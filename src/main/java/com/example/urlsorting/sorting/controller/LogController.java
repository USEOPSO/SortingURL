package com.example.urlsorting.sorting.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlsorting.sorting.dto.request.LogRequestDto;
import com.example.urlsorting.sorting.dto.request.SortRequestDto;
import com.example.urlsorting.sorting.dto.response.LogResponseDto;
import com.example.urlsorting.sorting.entities.Log;
import com.example.urlsorting.sorting.repository.LogRepository;
import com.example.urlsorting.sorting.service.LogService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {
	private final LogService logService;

	@PostMapping("/")
	public List<Log> listableLogs(@RequestBody LogRequestDto request){
		return logService.listableLogs(request);
	}
}
