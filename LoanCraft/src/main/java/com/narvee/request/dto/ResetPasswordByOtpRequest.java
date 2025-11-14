package com.narvee.request.dto;

import lombok.Data;

@Data
//Request to reset password using token
public class ResetPasswordByOtpRequest {
    private String token;
    private String newPassword;

    private String email;


}
