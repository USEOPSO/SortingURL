package com.example.urlsorting.common.jwt;


import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.urlsorting.common.util.role.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {
	private final JwtConfig jwtConfig;
	public static final long REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 24; // 1day
	public static final long ACCESS_EXPIRATION_TIME = 1000L * 30; // 10 second

	public String createRefreshToken(Long userId, Role role) {
		return Jwts.builder()
			.setSubject("refreshToken")
			.claim("userId", userId) // name claim 설정
			.claim("role", role) // email claim 설정
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
			.signWith(jwtConfig.getSecretKey(),SignatureAlgorithm.HS256)
			.compact();
	}

	public String createAccessToken(Long userId, Role role) {
		return Jwts.builder()
			.setSubject("accessToken")
			.claim("userId", userId) // name claim 설정
			.claim("role", role) // email claim 설정
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
			.signWith(jwtConfig.getSecretKey(), SignatureAlgorithm.HS256)
			.compact();
	}
}
