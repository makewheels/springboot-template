package com.github.makewheels.springboottemplate.file.operate;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 文件操作日志
 */
@Data
@Document
public class FileOperateLog {
    @Id
    private String id;

    private String operationType;
    private Date createTime;
    private String operator;
}
