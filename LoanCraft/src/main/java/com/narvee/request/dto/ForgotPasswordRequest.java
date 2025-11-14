package com.narvee.request.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
//Request to initiate password reset
public class ForgotPasswordRequest {
    @JsonAlias({"email", "Email"})
    private String email;
}