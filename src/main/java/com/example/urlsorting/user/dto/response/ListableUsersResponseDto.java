package com.example.urlsorting.user.dto.response;

import com.example.urlsorting.common.util.role.Role;
import com.example.urlsorting.user.entities.User;

import lombok.Getter;

@Getter
public class ListableUsersResponseDto {
	private final Long userId;
	private final String name;
	private final String email;
	private final Role role;

	public ListableUsersResponseDto(User user) {
		this.userId = user.getUserId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.role = user.getRole();
	}
}
