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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.urlsorting.common.util.exceprion.SortNotFoundException;
import com.example.urlsorting.common.util.exceprion.UserNotFoundException;
import com.example.urlsorting.common.util.page.Page;
import com.example.urlsorting.common.util.page.PageResponseDto;
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
		// Sort getSort = sortRepository.findById(sortId).orElseThrow(() -> new SortNotFoundException("Not Found Sort"));
		// Sort getSort = jpaQueryFactory.selectFrom(sort1).where(sort1.sortId.eq(sortId)).fetchOne();
		// return new SortResponseDto(getSort);

		TestSortResponseDto getSort = jpaQueryFactory
			.select(Projections.fields(TestSortResponseDto.class, sort1, ExpressionUtils.as(JPAExpressions.select(log))))
			.from(sort1)
			.leftJoin(sort1.logs, log).fetchJoin()
			.where(sort1.sortId.eq(sortId))
			.fetchOne();
		if(getSort == null) {
			throw new SortNotFoundException("Not Found Sort");
		}
		return new TestSortResponseDto(getSort);

		// TestSortResponseDto sorts = jpaQueryFactory
		// 	.select(Projections.bean(TestSortResponseDto.class, sort1))
		// 	.from(sort1)
		// 	.leftJoin(sort1.logs, log).fetchJoin()
		// 	.where(sort1.sortId.eq(sortId))
		// 	.fetchOne();

		// TestSortResponseDto sorts = jpaQueryFactory
		// 	.select(Projections.fields(TestSortResponseDto.class,
		// 		sort1.sortId.as("sortId"),
		// 		sort1.destination.as("destination"),
		// 		sort1.sort.as("sort"),
		// 		sort1.createAt.as("createAt"),
		// 		sort1.clickCnt.as("clickCnt"),
		// 		sort1.lastClickAt.as("lastClickAt"),
		// 		ExpressionUtils.as(JPAExpressions
		// 			.select(log)
		// 			.from(log)
		// 			.where(log.sort.eq(sort1)), "logs")))
		// 	.from(sort1)
		// 	.where(sort1.sortId.eq(sortId))
		// 	.fetchOne();

		// if(sorts == null) {
		// 	throw new SortNotFoundException("Not Found Sort");
		// }
		// return sorts;
	}

	public List<SortResponseDto> listableSorts(ListableSortsRequestDto request) throws Exception{
		List<Sort> sorts = sortRepository.findByUser(request.getUserId());
		if (sorts.isEmpty()) {
			return new ArrayList<>();
		}

		PageResponseDto page = Page.page(request, sorts);

		return sorts.subList(page.getStart(), page.getEnd()).stream().map(sort-> new SortResponseDto(sort)).collect(Collectors.toList());
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
