package com.sparta.taskflow.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CommonDto<T> {

    private int statusCode;
    private String message;
    private T data;


    public CommonDto (int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }
    public CommonDto (int statusCode, String message, T data){
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
