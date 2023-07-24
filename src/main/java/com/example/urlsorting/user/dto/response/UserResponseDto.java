package com.example.urlsorting.user.dto.response;

import com.example.urlsorting.common.util.role.Role;
import com.example.urlsorting.user.entities.User;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserResponseDto {
	private final Long userId;
	private final String name;
	private final String email;
	private final Role role;
	private final String token;

	public UserResponseDto(User user) {
		this.userId = user.getUserId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.role = user.getRole();
		this.token = user.getToken();
	}
}
