package com.example.urlsorting.sorting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.urlsorting.sorting.dto.request.LogRequestDto;
import com.example.urlsorting.sorting.dto.response.LogResponseDto;
import com.example.urlsorting.sorting.entities.Log;
import com.example.urlsorting.sorting.repository.LogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {
	private final LogRepository logRepository;

	public List<LogResponseDto> listableLogs(LogRequestDto request) {
		List<Log> logs = logRepository.findBySortId(request.getSortId());
		return logs.stream().map(log-> new LogResponseDto(log)).collect(Collectors.toList());
	}
}
