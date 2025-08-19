package com.narvee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailBody {

    private String tomail;   
    private String subject;  
    private String text;    
}
