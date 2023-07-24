package com.example.urlsorting.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.urlsorting.common.jwt.JwtFilter;
import com.example.urlsorting.common.jwt.JwtProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private JwtProvider jwtProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests()
				.requestMatchers("/**").permitAll() // anyone can access /register
				// .requestMatchers("/**").hasAnyAuthority()
				.anyRequest().authenticated();
		return http.build();
	}
}
