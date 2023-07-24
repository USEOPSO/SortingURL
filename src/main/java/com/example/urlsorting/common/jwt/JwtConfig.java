package com.example.urlsorting.common.jwt;

import java.security.Key;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtConfig {
	@Value("${jwt.secret}")
	private String secretKey;

	public Key getSecretKey() {
		byte[] decodedKey = Base64.getDecoder().decode(secretKey);
		return Keys.hmacShaKeyFor(decodedKey);
	}
}
