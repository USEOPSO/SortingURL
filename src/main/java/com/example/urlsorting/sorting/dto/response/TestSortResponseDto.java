package com.example.urlsorting.sorting.dto.response;

import java.util.Date;
import java.util.List;

import com.example.urlsorting.sorting.entities.Sort;

import lombok.Getter;

@Getter
public class TestSortResponseDto {
	private final Long sortId;
	private final String destination;
	private final String sort;
	private final Date createAt;
	private final int clickCnt;
	private final Date lastClickAt;
	private List<TestLogResponseDto> logs;

	public TestSortResponseDto(Sort sort) {
		this.sortId = sort.getSortId();
		this.destination = sort.getDestination();
		this.sort = sort.getSort();
		this.createAt = sort.getCreateAt();
		this.clickCnt = sort.getClickCnt();
		this.lastClickAt = sort.getLastClickAt();
	}

	public void setLogs(List<TestLogResponseDto> logs) {
		this.logs = logs;
	}

}
