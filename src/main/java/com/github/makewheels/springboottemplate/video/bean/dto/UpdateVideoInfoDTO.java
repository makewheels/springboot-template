package com.github.makewheels.springboottemplate.video.bean.dto;

import lombok.Data;

/**
 * 更新视频信息
 */
@Data
public class UpdateVideoInfoDTO {
    private String id;
    private String title;
    private String description;
}
