package com.example.urlsorting.common.util.page;

import java.util.List;

import com.example.urlsorting.sorting.dto.request.ListableSortsRequestDto;

public class Page {
	public static PageResponseDto page(ListableSortsRequestDto request, List<?> entity) throws Exception{
		int start = request.getPage() * request.getSize();
		int end = Math.min(start + request.getSize(), entity.size());

		if (start > end) {
			throw new Exception("Page number is out of range");
		}

		return new PageResponseDto(start, end);
	}
}
