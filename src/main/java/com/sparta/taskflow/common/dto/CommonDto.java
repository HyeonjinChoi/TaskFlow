package com.sparta.taskflow.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonDto<T> {

    private int statusCode;
    private String message;
    private T data;


}
