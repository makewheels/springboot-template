package com.github.makewheels.springboottemplate.system.context;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class Context {
    private String videoId;
    private String clientId;
    private String sessionId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
