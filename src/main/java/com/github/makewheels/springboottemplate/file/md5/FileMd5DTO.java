package com.github.makewheels.springboottemplate.file.md5;

import lombok.Data;

@Data
public class FileMd5DTO {
    private String fileId;
    private String key;
    private String md5;
}
