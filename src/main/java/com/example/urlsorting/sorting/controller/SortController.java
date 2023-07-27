package com.example.urlsorting.sorting.controller;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.urlsorting.sorting.dto.request.ListableSortsRequestDto;
import com.example.urlsorting.sorting.dto.request.SortRequestDto;
import com.example.urlsorting.sorting.dto.response.SortResponseDto;
import com.example.urlsorting.sorting.dto.response.TestSortResponseDto;
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
	public TestSortResponseDto getSort(@PathVariable("sortId") Long sortId) throws Exception {
		return sortService.getSort(sortId);
	}

	@GetMapping("/")
	public Page<SortResponseDto> listableSorts(ListableSortsRequestDto request, Pageable pageable) throws Exception{
		return sortService.listableSorts(request, pageable);
	}

	@PostMapping("/sortingUrl")
	public SortResponseDto sortingUrl(@RequestBody SortRequestDto request) throws Exception{
		return sortService.sortingUrl(request);
	}

	@GetMapping("/sortingUrl/{sortId}")
	public SortResponseDto redirectUrl(@PathVariable Long sortId, HttpServletResponse response, HttpServletRequest request) throws Exception {
		return sortService.redirectUrl(sortId, response, request);
	}

}
