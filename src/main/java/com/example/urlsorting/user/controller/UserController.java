package com.example.urlsorting.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlsorting.user.dto.request.UserRequestDto;
import com.example.urlsorting.user.dto.response.LoginResponseDto;
import com.example.urlsorting.user.dto.response.UserResponseDto;
import com.example.urlsorting.user.entities.User;
import com.example.urlsorting.user.repository.UserRepository;
import com.example.urlsorting.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserRepository userRepository;

	@GetMapping("/{userId}")
	public User getUser(@PathVariable("userId") Long userId){
		HttpHeaders headers = new HttpHeaders();
		return userRepository.findByUserId(userId);
	}

	@GetMapping()
	public List<User> listableUsers(){
		List<User> users = userRepository.findAll();

		if (users.isEmpty()) {
			return new ArrayList<>();
		}
		return users;
	}

	@PostMapping("/sign-in")
	public UserResponseDto registerUser(@RequestBody UserRequestDto request) {
		return userService.registerUser(request);
	}

	@PostMapping("/login")
	public LoginResponseDto loginUser(@RequestBody UserRequestDto request) throws Exception {
		return userService.loginUser(request);
	}

}
