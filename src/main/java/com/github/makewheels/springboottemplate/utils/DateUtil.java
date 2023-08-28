package com.github.makewheels.springboottemplate.utils;

import java.util.Date;

public class DateUtil {
    public static String getCurrentTimeString() {
        return cn.hutool.core.date.DateUtil.formatDateTime(new Date());
    }
}
