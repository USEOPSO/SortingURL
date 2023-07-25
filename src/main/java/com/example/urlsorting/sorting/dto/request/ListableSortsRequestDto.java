package com.example.urlsorting.sorting.dto.request;

import com.example.urlsorting.user.entities.User;

import lombok.Getter;

@Getter
public class ListableSortsRequestDto {
	private final Long userId;
	private final int page;
	private final int size;

	public ListableSortsRequestDto(Long userId, int page, int size) {
		this.userId = userId;
		this.page = page - 1;
		this.size = size;
	}
}
