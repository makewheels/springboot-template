package com.github.makewheels.springboottemplate.etc.miniprogram;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.model.OSSObject;
import com.github.makewheels.springboottemplate.file.FileService;
import com.github.makewheels.springboottemplate.file.bean.File;
import com.github.makewheels.springboottemplate.file.constants.FileType;
import com.github.makewheels.springboottemplate.system.response.Result;
import com.github.makewheels.springboottemplate.user.UserHolder;
import com.github.makewheels.springboottemplate.user.bean.User;
import com.github.makewheels.springboottemplate.video.VideoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.Duration;

@Service
public class MiniProgramService {
    @Value("${wechat.mini-program.env}")
    private String miniProgramEnv;
    @Value("${wechat.mini-program.AppID}")
    private String appId;
    @Value("${wechat.mini-program.AppSecret}")
    private String appSecret;

    @Resource
    private FileService fileService;
    @Resource
    private VideoRepository videoRepository;
    @Resource
    private MongoTemplate mongoTemplate;

    private String accessToken;
    private long accessTokenExpireAt;

    /**
     * {"access_token":"63_3U8e8xSlI6nv-2sRghMQW2bUDy34dzyZBsTi-lX02tNxMcEU9x769TpG375VqzmCfVzb
     * rp9XBr_2n2CJ1ZoEHDDEauRKUIWaI-fNRI-1yvH0P57i8xOPnIKoK2QNSHhAJAWBQ",
     * "expires_in":7200}
     */
    private String getAccessToken() {
        //如果已经有了，并且没过期，直接返回
        if (accessToken != null && System.currentTimeMillis() < accessTokenExpireAt) {
            return accessToken;
        }
        //否则请求微信
        String json = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token"
                + "?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret);
        JSONObject jsonObject = JSON.parseObject(json);
        accessToken = jsonObject.getString("access_token");
        accessTokenExpireAt = System.currentTimeMillis() + jsonObject.getInteger("expires_in") * 1000;
        return accessToken;
    }

    private InputStream getQrCodeInputStream(String videoId) {
        // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
        JSONObject param = new JSONObject();
        param.put("scene", videoId);
        param.put("page", "pages/share/share");
        param.put("width", 300);
        if (miniProgramEnv.equals("dev")) {
            param.put("check_path", false);
//            param.put("env_version", "develop");
//            param.put("env_version", "trial");
        }
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + getAccessToken();
        HttpResponse response = HttpUtil.createPost(url).body(param.toJSONString()).execute();
        return response.bodyStream();
    }

    /**
     * 登录
     * {
     * "session_key": "tQY+38pdTVnAIWCBNiM1+A==",
     * "openid": "o--sB5rdWBfwTidbZzzn4FXfWpEg"
     * }
     * <p>
     * {
     * "errcode": 40163,
     * "errmsg": "code been used, rid: 6380622c-50076416-224b40b0"
     * }
     */
    public Result<JSONObject> login(String jscode) {
        User user = UserHolder.get();

        JSONObject json = JSON.parseObject(HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session" +
                "?appid=" + appId + "&secret=" + appSecret + "&js_code=" + jscode));
        String openid = json.getString("openid");
        String sessionKey = json.getString("session_key");
        if (openid == null) {
            throw new RuntimeException("获取不到openid, jscode = " + jscode);
        }
        return null;
    }
}
