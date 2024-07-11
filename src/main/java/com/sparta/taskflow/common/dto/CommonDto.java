package com.sparta.taskflow.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonDto<T> {
	private final Integer status;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final T data;
}
