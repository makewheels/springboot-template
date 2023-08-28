package com.github.makewheels.springboottemplate.video.bean.dto;

import lombok.Data;

/**
 * 更新播放设置
 */
@Data
public class UpdateWatchSettingsDTO {
    private String id;
    private Boolean showUploadTime;
    private Boolean showWatchCount;
}
