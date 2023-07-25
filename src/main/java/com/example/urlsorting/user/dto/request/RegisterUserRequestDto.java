package com.example.urlsorting.user.dto.request;

import com.example.urlsorting.common.util.role.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterUserRequestDto {
	private Long userId;
	private String name;
	private String email;
	private String password;
	private Role role;
}
