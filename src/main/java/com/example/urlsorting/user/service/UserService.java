package com.example.urlsorting.user.service;

import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.urlsorting.common.jwt.JwtConfig;
import com.example.urlsorting.common.jwt.JwtProvider;
import com.example.urlsorting.common.util.exceprion.NotMatchPasswordException;
import com.example.urlsorting.common.util.exceprion.TokenExpiredException;
import com.example.urlsorting.common.util.role.Role;
import com.example.urlsorting.common.util.exceprion.UserNotFoundException;
import com.example.urlsorting.user.dto.request.UserRequestDto;
import com.example.urlsorting.user.dto.response.LoginResponseDto;
import com.example.urlsorting.user.dto.response.UserResponseDto;
import com.example.urlsorting.user.entities.User;
import com.example.urlsorting.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
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

	public UserResponseDto registerUser(UserRequestDto request) {
		Role role = request.getRole() != null ? request.getRole() : Role.USER;
		String token = jwtProvider.createRefreshToken(request.getEmail(), role); // 토큰생성

		// 입력받은 user 정보 db insert
		User instUser = User.builder()
			.name(request.getName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(role)
			.token(token)
			.build();
		User savedUser = userRepository.save(instUser);

		return new UserResponseDto(savedUser);
	}

	public LoginResponseDto loginUser(UserRequestDto request) throws Exception {
		String password = request.getPassword();
		String token = null;

		// 옵셔널로 해서 user가 있고 없고 체크
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("Not Found User"));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			// 로그인 실패
			throw new NotMatchPasswordException("Not Match Password"); // 실패 메시지를 JSON 형태로 응답
		}

		// refreshToken 만료 확인
		boolean chkRefreshToken = chkRefreshToken(user.getUserId());
		if(!chkRefreshToken) {
			throw new TokenExpiredException("Token Expired");
		}

		// accessToken 생성
		token = jwtProvider.createAccessToken(user.getUserId(), user.getRole());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		return LoginResponseDto.builder().email(user.getEmail()).token(token).build();
	}

	// refreshToken 만료시간 확인
	private boolean chkRefreshToken(Long userId) {
		User user = userRepository.findByUserId(userId);
		String token = user.getToken();

		try {
			Jwts.parser()
				.setSigningKey(jwtConfig.getSecretKey())
				.parseClaimsJws(token)
				.getBody();
			return true;
		} catch (JwtException e) {
			e.printStackTrace();
			// 토큰 파싱 실패
			return false;
		}
	}
}
