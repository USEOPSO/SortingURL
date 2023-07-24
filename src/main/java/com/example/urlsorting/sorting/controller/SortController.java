package com.example.urlsorting.sorting.controller;


import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlsorting.sorting.dto.request.SortRequestDto;
import com.example.urlsorting.sorting.dto.response.SortResponseDto;
import com.example.urlsorting.sorting.entities.Sort;
import com.example.urlsorting.sorting.repository.SortRepository;
import com.example.urlsorting.sorting.service.SortService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/sort")
@RequiredArgsConstructor
public class SortController {

	private final SortRepository sortRepository;
	private final SortService sortService;

	@GetMapping("/{sortId}")
	public Sort getSort(@PathVariable("sortId") Long sortId) {
		return sortRepository.findBySortId(sortId);
	}

	@PostMapping("/sortingUrl")
	public SortResponseDto sortingUrl(@RequestBody SortRequestDto request)	{
		return sortService.sortingUrl(request);
	}

	@GetMapping("/sortingUrl/{sortId}")
	public void redirectUrl(@PathVariable Long sortId, HttpServletResponse response, HttpServletRequest request) throws IOException {
		sortService.redirectUrl(sortId, response, request);
	}

}
