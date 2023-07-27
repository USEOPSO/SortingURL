package com.example.urlsorting.sorting.dto.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.urlsorting.sorting.entities.Sort;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestSortResponseDto {
	private final Long sortId;
	private final String destination;
	private final String sort;
	private final Date createAt;
	private final int clickCnt;
	private final Date lastClickAt;
	private final List<TestLogResponseDto> logs;;

	public TestSortResponseDto(Sort sort) {
		this.sortId = sort.getSortId();
		this.destination = sort.getDestination();
		this.sort = sort.getSort();
		this.createAt = sort.getCreateAt();
		this.clickCnt = sort.getClickCnt();
		this.lastClickAt = sort.getLastClickAt();
		// this.logs = sort.getLogs().stream().map(TestLogResponseDto::new).collect(Collectors.toList());;
		this.logs = Optional.ofNullable(sort.getLogs())
			.orElseGet(Collections::emptyList)
			.stream()
			.map(TestLogResponseDto::new)
			.collect(Collectors.toList());
	}

	// public TestSortResponseDto(Long sortId, String destination, String sort, Date createAt, int clickCnt,
	// 	Date lastClickAt,
	// 	List<TestLogResponseDto> logs) {
	// 	this.sortId = sortId;
	// 	this.destination = destination;
	// 	this.sort = sort;
	// 	this.createAt = createAt;
	// 	this.clickCnt = clickCnt;
	// 	this.lastClickAt = lastClickAt;
	// 	this.logs = Optional.ofNullable(logs)
	// 			.orElseGet(Collections::emptyList)
	// 			.stream()
	// 			.map(TestLogResponseDto::new)
	// 			.collect(Collectors.toList());;
	// }
}
