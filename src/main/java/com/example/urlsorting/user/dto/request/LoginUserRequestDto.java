package com.example.urlsorting.user.dto.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserRequestDto {
	private String email;
	private String password;
}
