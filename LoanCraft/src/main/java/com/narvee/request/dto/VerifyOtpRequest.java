package com.narvee.request.dto;

//file: com.narvee/request/dto/VerifyOtpRequest.java


import lombok.Data;

@Data
public class VerifyOtpRequest {
 private String email;
 private String otp;
}