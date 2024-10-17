package com.example.VNPay_Api_Integration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private HttpStatusCode httpStatusCode;
    private String errorCode;
    private String message;
    private Map<String, Object> details;
}
