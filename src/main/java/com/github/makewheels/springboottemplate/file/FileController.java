package com.github.makewheels.springboottemplate.file;

import com.alibaba.fastjson.JSONObject;
import com.github.makewheels.springboottemplate.system.context.RequestUtil;
import com.github.makewheels.springboottemplate.system.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("file")
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 获取上传凭证
     */
    @GetMapping("getUploadCredentials")
    public Result<JSONObject> getUploadCredentials(@RequestParam String fileId) {
        JSONObject uploadCredentials = fileService.getUploadCredentials(fileId);
        return Result.ok(uploadCredentials);
    }

    /**
     * 当前文件上传完成时
     */
    @GetMapping("uploadFinish")
    public Result<Void> uploadFinish(@RequestParam String fileId) {
        fileService.uploadFinish(fileId);
        return Result.ok();
    }

    /**
     * 访问文件
     */
    @GetMapping("access")
    public Result<Void> access(
            @RequestParam String resolution, @RequestParam String fileId, @RequestParam String timestamp,
            @RequestParam String nonce, @RequestParam String sign) {
        fileService.access(RequestUtil.getContext(), resolution, fileId, timestamp, nonce, sign);
        return Result.ok();
    }

}
