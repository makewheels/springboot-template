package com.github.makewheels.springboottemplate.video.bean.entity;

import lombok.Data;

/**
 * 视频链接，源视频文件md5已存在
 */
@Data
public class Link {
    private Boolean hasLink;
    private String linkVideoId;

    public Link() {
        this.hasLink = false;
    }
}
