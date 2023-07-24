package com.example.urlsorting.sorting.dto.response;

import java.util.Date;

import com.example.urlsorting.sorting.entities.Sort;
import com.example.urlsorting.user.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortResponseDto {
	private String destination;
	private String sort;
	private Date createAt;
	private int clickCnt;
	private Date lastClickAt;
}
