package com.github.makewheels.springboottemplate.user.bean;

import lombok.Data;

@Data
public class VerificationCode {
    private String phone;
    private String code;
}
