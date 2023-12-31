package com.github.makewheels.springboottemplate.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;

/**
 * 签名工具类
 */
public class SignUtil {
    public static String hmac(String key, String data) {
        HMac hMac = SecureUtil.hmacSha256(key);
        return hMac.digestBase64(data, true);
    }
}
