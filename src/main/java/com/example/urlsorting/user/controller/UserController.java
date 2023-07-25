package com.example.urlsorting.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlsorting.user.dto.request.LoginUserRequestDto;
import com.example.urlsorting.user.dto.request.RegisterUserRequestDto;
import com.example.urlsorting.user.dto.response.ListableUsersResponseDto;
import com.example.urlsorting.user.dto.response.LoginResponseDto;
import com.example.urlsorting.user.dto.response.RegisterUserResponseDto;
import com.example.urlsorting.user.dto.response.UserResponseDto;
import com.example.urlsorting.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/{userId}")
	public UserResponseDto getUser(@PathVariable("userId") Long userId) throws Exception {
		return userService.getUser(userId);
	}

	@GetMapping("/")
	public List<ListableUsersResponseDto> listableUsers() {
		return userService.listableUsers();
	}

	@PostMapping("/sign-in")
	public RegisterUserResponseDto registerUser(@RequestBody RegisterUserRequestDto request) {
		return userService.registerUser(request);
	}

	@PostMapping("/login")
	public LoginResponseDto loginUser(@RequestBody LoginUserRequestDto request) throws Exception {
		return userService.loginUser(request);
	}

}
