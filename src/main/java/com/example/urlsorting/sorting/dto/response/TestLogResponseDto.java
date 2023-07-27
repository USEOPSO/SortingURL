package com.example.urlsorting.sorting.dto.response;

import java.util.Date;

import com.example.urlsorting.sorting.entities.Log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TestLogResponseDto {
	private final Long logId;
	private final String ip;
	private final Date creatAt;

	public TestLogResponseDto(Log log) {
		this.logId = log.getLogId();
		this.ip = log.getIp();
		this.creatAt = log.getCreateAt();
	}
}
