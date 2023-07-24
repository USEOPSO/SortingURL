package com.example.urlsorting.common.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		System.out.println(111);
		System.out.println(authorizationHeader);

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			try {
				// email과 role 가져옴
				Authentication authentication = jwtProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (ExpiredJwtException e) {
				// Todo 만료된 refreshedToken을 재발급(email, role)
				// String refreshedToken = jwtProvider.createRefreshToken(token);
				// response.setHeader("Authorization", "Bearer " + refreshedToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
