package com.example.urlsorting.common.util.page;

import lombok.Getter;

@Getter
public class PageResponseDto {
	private final int start;
	private final int end;

	public PageResponseDto(int start, int end) {
		this.start = start;
		this.end = end;
	}
}
