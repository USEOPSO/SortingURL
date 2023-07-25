package com.example.urlsorting.sorting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlsorting.sorting.dto.request.LogRequestDto;
import com.example.urlsorting.sorting.dto.response.LogResponseDto;
import com.example.urlsorting.sorting.service.LogService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {
	private final LogService logService;

	@GetMapping("/")
	public List<LogResponseDto> listableLogs(LogRequestDto request){
		return logService.listableLogs(request);
	}
}
