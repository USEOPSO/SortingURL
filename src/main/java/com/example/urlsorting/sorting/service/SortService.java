package com.example.urlsorting.sorting.service;

import static com.example.urlsorting.sorting.entities.QLog.*;
import static com.example.urlsorting.sorting.entities.QSort.*;
import static com.example.urlsorting.user.entities.QUser.*;

import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.urlsorting.common.util.exceprion.SortNotFoundException;
import com.example.urlsorting.common.util.exceprion.UserNotFoundException;
import com.example.urlsorting.sorting.dto.request.ListableSortsRequestDto;
import com.example.urlsorting.sorting.dto.request.SortRequestDto;
import com.example.urlsorting.sorting.dto.response.LogResponseDto;
import com.example.urlsorting.sorting.dto.response.SortResponseDto;
import com.example.urlsorting.sorting.dto.response.TestLogResponseDto;
import com.example.urlsorting.sorting.dto.response.TestSortResponseDto;
import com.example.urlsorting.sorting.entities.Log;
import com.example.urlsorting.sorting.entities.Sort;
import com.example.urlsorting.sorting.repository.LogRepository;
import com.example.urlsorting.sorting.repository.SortRepository;
import com.example.urlsorting.user.entities.User;
import com.example.urlsorting.user.repository.UserRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SortService {
	private final SortRepository sortRepository;
	private final LogRepository logRepository;
	private final UserRepository userRepository;
	private final JPAQueryFactory jpaQueryFactory;
	private static final String BASE_URL = "http://localhost:8080/sort/sortingUrl/";

	public TestSortResponseDto getSort(Long sortId) throws Exception{
		// sort 조회
		TestSortResponseDto getSort = jpaQueryFactory
			.select(Projections.constructor(TestSortResponseDto.class, sort1))
			.from(sort1)
			.where(sort1.sortId.eq(sortId))
			.fetchOne();

		List<TestLogResponseDto> listableLogs = jpaQueryFactory
			.select(Projections.constructor(TestLogResponseDto.class, log))
			.from(log)
			.where(log.sort.sortId.eq(getSort.getSortId()))
			.fetch();

		getSort.setLogs(listableLogs);

		if(getSort == null) {
			throw new SortNotFoundException("Not Found Sort");
		}

		return getSort;
	}

	public Page<SortResponseDto> listableSorts(ListableSortsRequestDto request, Pageable pageable) throws Exception{
		pageable = PageRequest.of(request.getPage(), request.getSize());

		QueryResults<SortResponseDto> results = jpaQueryFactory
			.select(Projections.constructor(SortResponseDto.class,sort1))
			.from(sort1)
			.where(sort1.user.userId.eq(request.getUserId()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		List<SortResponseDto> listableSorts = results.getResults();

		if (listableSorts.isEmpty()) {
			return new PageImpl<>(new ArrayList<>(), pageable, results.getTotal());
		}
		return new PageImpl<>(listableSorts, pageable, results.getTotal());
	}

	@Transactional
	public SortResponseDto sortingUrl(SortRequestDto request) throws Exception {
		String originalURL = request.getDestination();
		User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserNotFoundException("Not Found User"));

		Sort instSort = Sort.builder()
			.destination(originalURL)
			.user(user)
			.build();
		Sort savedSort = sortRepository.save(instSort);

		String url = BASE_URL + savedSort.getSortId();
		savedSort.setSort(url);
		savedSort.setCreateAt(Date.from(Instant.now()));

		return new SortResponseDto(savedSort);
	}

	@Transactional
	public SortResponseDto redirectUrl(Long sortId, HttpServletResponse response, HttpServletRequest request) throws Exception {
		Sort getSort = sortRepository.findById(sortId).orElseThrow(() -> new SortNotFoundException("Not Found Sort"));
		String originalURL = getSort.getDestination();
		InetAddress localhost = InetAddress.getLocalHost();
		String ipAddress = localhost.getHostAddress();

		// 마지막 클릭 일자, 클릭 카운트
		getSort.lastClickAt(Date.from(Instant.now()));
		getSort.clickCnt(getSort.getClickCnt());
		sortRepository.save(getSort);

		// 클릭 log 생성
		Log instLog = Log.builder().ip(ipAddress).createAt(Date.from(Instant.now())).sort(getSort).build();
		logRepository.save(instLog);
		response.sendRedirect(originalURL);
		return new SortResponseDto(getSort);
	}
}
