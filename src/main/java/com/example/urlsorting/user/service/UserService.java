package com.example.urlsorting.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.urlsorting.common.jwt.JwtConfig;
import com.example.urlsorting.common.jwt.JwtProvider;
import com.example.urlsorting.common.util.exceprion.NotMatchPasswordException;
import com.example.urlsorting.common.util.exceprion.TokenExpiredException;
import com.example.urlsorting.common.util.role.Role;
import com.example.urlsorting.common.util.exceprion.UserNotFoundException;
import com.example.urlsorting.user.dto.request.LoginUserRequestDto;
import com.example.urlsorting.user.dto.request.RegisterUserRequestDto;
import com.example.urlsorting.user.dto.response.ListableUsersResponseDto;
import com.example.urlsorting.user.dto.response.LoginResponseDto;
import com.example.urlsorting.user.dto.response.RegisterUserResponseDto;
import com.example.urlsorting.user.dto.response.UserResponseDto;
import com.example.urlsorting.user.entities.User;
import com.example.urlsorting.user.repository.UserRepository;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final JwtConfig jwtConfig;

	public UserResponseDto getUser(Long userId) throws Exception{
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Not Found User"));
		return new UserResponseDto(user);
	}

	public List<ListableUsersResponseDto> listableUsers() {
		// 필요한 response userId, name, email, role
		List<User> users = userRepository.findAll();
		return users.stream().map(user-> new ListableUsersResponseDto(user)).collect(Collectors.toList());
	}

	@Transactional
	public RegisterUserResponseDto registerUser(RegisterUserRequestDto request) {
		// request에 role이 없으면 USER를 넣고 있으면 입력받은 role 적용
		Role role = request.getRole() != null ? request.getRole() : Role.USER;

		// 입력받은 user 정보 db insert
		User instUser = User.builder()
			.name(request.getName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(role)
			.build();
		User savedUser = userRepository.save(instUser);

		String token = jwtProvider.createRefreshToken(savedUser.getUserId(), savedUser.getRole()); // 토큰생성
		savedUser.setToken(token);

		return new RegisterUserResponseDto(savedUser);
	}

	public LoginResponseDto loginUser(LoginUserRequestDto request) throws Exception {
		String password = request.getPassword();

		// 옵셔널로 해서 user가 있고 없고 체크
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("Not Found User"));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			// 로그인 실패
			throw new NotMatchPasswordException("Not Match Password"); // 실패 메시지를 JSON 형태로 응답
		}

		// refreshToken 만료 확인 후 만료 되었을때 자동 재발급
		boolean chkRefreshToken = chkRefreshToken(user);
		if(!chkRefreshToken) {
			throw new TokenExpiredException("refreshToken Expired reissueToken");
		}

		// accessToken 생성
		String token = jwtProvider.createAccessToken(user.getUserId(), user.getRole());

		return new LoginResponseDto(user.getEmail(), token);
	}

	// refreshToken 만료시간 확인
	private boolean chkRefreshToken(User user) {
		String token = user.getToken();

		try {
			Jwts.parser()
				.setSigningKey(jwtConfig.getSecretKey())
				.parseClaimsJws(token)
				.getBody();
			return true;
		} catch (JwtException e) {
			// jwt expiredTime 도래시 새로운 refreshToken 발급
			String reissueToken = jwtProvider.createRefreshToken(user.getUserId(), user.getRole()); // 토큰생성
			System.out.println(reissueToken);
			user.setToken(reissueToken);
			userRepository.save(user);
			// 토큰 파싱 실패
			return false;
		}
	}
}
