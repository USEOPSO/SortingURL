package com.example.urlsorting.sorting.service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.urlsorting.sorting.dto.request.SortRequestDto;
import com.example.urlsorting.sorting.dto.response.SortResponseDto;
import com.example.urlsorting.sorting.entities.Log;
import com.example.urlsorting.sorting.entities.Sort;
import com.example.urlsorting.sorting.repository.LogRepository;
import com.example.urlsorting.sorting.repository.SortRepository;
import com.example.urlsorting.user.dto.response.LoginResponseDto;
import com.example.urlsorting.user.entities.User;
import com.example.urlsorting.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SortService {
	private final SortRepository sortRepository;
	private final LogRepository logRepository;
	private final UserRepository userRepository;
	private static final String BASE_URL = "http://localhost:8080/sor.t/sortingUrl/";

	@Transactional
	public SortResponseDto sortingUrl(SortRequestDto request) {
		String originalURL = request.getDestination();
		String token = request.getToken();

		User user = userRepository.findByToken(token);

		Sort instSort = Sort.builder()
			.destination(originalURL)
			.user(user)
			.build();
		Sort savedSort = sortRepository.save(instSort);

		Sort sort = sortRepository.findByUserAndDestination(user, savedSort.getDestination());

		String url = BASE_URL + sort.getSortId();
		instSort.setSort(url);
		instSort.setCreateAt(Date.from(Instant.now()));
		Sort saveSort = sortRepository.save(instSort);

		return SortResponseDto.builder()
			.destination(saveSort.getDestination())
			.sort(saveSort.getSort())
			.createAt(saveSort.getCreateAt())
			.clickCnt(saveSort.getClickCnt())
			.lastClickAt(saveSort.getLastClickAt())
			.build();
	}

	@Transactional
	public void redirectUrl(Long sortId, HttpServletResponse response, HttpServletRequest request) throws IOException {
		Optional<Sort> getSort = sortRepository.findById(sortId);
		Sort sort = getSort.get();
		String originalURL = sort.getDestination();

		// 마지막 클릭 일자, 클릭 카운트
		sort.lastClickAt(Date.from(Instant.now()));
		sort.clickCnt(sort.getClickCnt() + 1);
		sortRepository.save(sort);

		// 클릭 log 생성
		Log instLog = Log.builder().ip(request.getRemoteAddr()).createAt(Date.from(Instant.now())).sort(sort).build();
		logRepository.save(instLog);
		response.sendRedirect(originalURL);
	}
}
