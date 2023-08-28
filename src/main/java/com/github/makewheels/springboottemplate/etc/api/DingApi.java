package com.github.makewheels.springboottemplate.etc.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DingApi extends Api {
    private String title;
    private String text;
    private String messageType;
}
