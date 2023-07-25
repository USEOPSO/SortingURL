package com.example.urlsorting.user.dto.response;

import lombok.Getter;

@Getter
public class LoginResponseDto {
	private final String email;
	private final String token;

	public LoginResponseDto(String email, String token) {
		this.email = email;
		this.token = token;
	}

}
