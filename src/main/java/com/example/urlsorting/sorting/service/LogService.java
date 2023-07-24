package com.example.urlsorting.sorting.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.urlsorting.sorting.dto.request.LogRequestDto;
import com.example.urlsorting.sorting.dto.response.LogResponseDto;
import com.example.urlsorting.sorting.entities.Log;
import com.example.urlsorting.sorting.entities.Sort;
import com.example.urlsorting.sorting.repository.LogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {
	private final LogRepository logRepository;

	public List<Log> listableLogs(LogRequestDto request) {
		return logRepository.findBySortId(request.getSortId());
	}
}
