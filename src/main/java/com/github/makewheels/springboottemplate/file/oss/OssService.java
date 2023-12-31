package com.github.makewheels.springboottemplate.file.oss;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OssService {
    @Value("${aliyun.oss.bucket}")
    private String bucket;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.secretKey}")
    private String secretKey;

    private OSS ossClient;

    /**
     * 获取client
     */
    private OSS getClient() {
        if (ossClient != null) return ossClient;
        ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
        configuration.setProtocol(Protocol.HTTPS);
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, secretKey, configuration);
        return ossClient;
    }

    /**
     * 关闭client
     */
    public void shutdownClient() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    /**
     * 获取临时上传凭证
     */
    public JSONObject getUploadCredentials(String key) {
        DefaultProfile.addEndpoint("cn-beijing", "Sts",
                "sts.cn-beijing.aliyuncs.com");
        IClientProfile profile = DefaultProfile.getProfile("cn-beijing", accessKeyId, secretKey);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        AssumeRoleRequest request = new AssumeRoleRequest();
        //精确定位上传权限
//        request.setPolicy("");
        request.setRoleArn("acs:ram::1618784280874658:role/role-oss-video-2022");
        request.setRoleSessionName("roleSessionName-" + IdUtil.simpleUUID());
        request.setDurationSeconds(60 * 60 * 3L);
        AssumeRoleResponse response = null;
        try {
            response = client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        JSONObject credentials = new JSONObject();
        credentials.put("bucket", bucket);
        credentials.put("key", key);
        credentials.put("endpoint", endpoint);
        if (response == null) return null;
        AssumeRoleResponse.Credentials responseCredentials = response.getCredentials();
        credentials.put("accessKeyId", responseCredentials.getAccessKeyId());
        credentials.put("secretKey", responseCredentials.getAccessKeySecret());
        credentials.put("sessionToken", responseCredentials.getSecurityToken());
        credentials.put("expiration", responseCredentials.getExpiration());
        return credentials;
    }

    /**
     * 上传
     */
    public PutObjectResult putObject(String key, InputStream inputStream) {
        log.info("阿里云OSS上传: key = {}", key);
        try {
            getClient().putObject(bucket, key, inputStream);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断object是否存在
     */
    public boolean doesObjectExist(String key) {
        return getClient().doesObjectExist(bucket, key);
    }

    /**
     * 获取单个文件
     */
    public OSSObject getObject(String key) {
        return getClient().getObject(bucket, key);
    }

    /**
     * 按照prefix查找，分页遍历，列举所有文件
     */
    public List<OSSObjectSummary> listAllObjects(String prefix) {
        List<OSSObjectSummary> objects = new ArrayList<>();
        String nextContinuationToken = null;
        ListObjectsV2Result result;
        do {
            ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request();
            listObjectsRequest.setBucketName(bucket);
            listObjectsRequest.withMaxKeys(1000);
            listObjectsRequest.setContinuationToken(nextContinuationToken);
            listObjectsRequest.setPrefix(prefix);
            result = getClient().listObjectsV2(listObjectsRequest);
            objects.addAll(result.getObjectSummaries());
            nextContinuationToken = result.getNextContinuationToken();
        } while (result.isTruncated());
        return objects;
    }

    /**
     * 删除文件
     */
    public VoidResult deleteObject(String key) {
        log.info("阿里云OSS删除文件: key = " + key);
        return getClient().deleteObject(bucket, key);
    }

    /**
     * 批量删除文件
     * TODO 需要分页
     */
    public DeleteObjectsResult deleteAllObjects(List<String> keys) {
        log.info("阿里云OSS批量删除文件: 请求keys = " + JSON.toString(keys));
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket);
        deleteObjectsRequest.setKeys(keys);
        DeleteObjectsResult deleteObjectsResult = getClient().deleteObjects(deleteObjectsRequest);
        log.info("阿里云OSS批量删除文件: 响应deleteObjectsResult = " + JSON.toString(deleteObjectsResult));
        return deleteObjectsResult;
    }

    /**
     * 预签名下载文件
     */
    public String generatePresignedUrl(String key, Duration duration) {
        Date expiration = new Date(System.currentTimeMillis() + duration.toMillis());
        return getClient().generatePresignedUrl(bucket, key, expiration, HttpMethod.GET).toString();
    }

    /**
     * 设置对象权限
     */
    public void setObjectAcl(String key, CannedAccessControlList cannedAccessControlList) {
        log.info("阿里云OSS设置对象权限, key = {}, cannedAccessControlList = {}",
                key, cannedAccessControlList);
        getClient().setObjectAcl(bucket, key, cannedAccessControlList);
    }

    /**
     * 重命名
     */
    public VoidResult renameObject(String sourceKey, String destinationKey) {
        return getClient().renameObject(bucket, sourceKey, destinationKey);
    }

    /**
     * 拷贝
     */
    public CopyObjectResult copy(String sourceKey, String destinationKey) {
        return getClient().copyObject(bucket, sourceKey, bucket, destinationKey);
    }

    /**
     * 改变object存储类型，通过覆盖key实现
     */
    public CopyObjectResult changeObjectStorageClass(String key, StorageClass storageClass) {
        log.info("阿里云OSS改变object存储类型, key = {}, storageClass = {}", key, storageClass);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket, key, bucket, key);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setHeader(OSSHeaders.OSS_STORAGE_CLASS, storageClass);
        copyObjectRequest.setNewObjectMetadata(meta);
        return getClient().copyObject(copyObjectRequest);
    }

    /**
     * 取回object
     */
    public RestoreObjectResult restoreObject(String key) {
        return getClient().restoreObject(bucket, key);
    }

    /**
     * 创建软连接
     */
    public VoidResult createSymlink(String symlink, String target) {
        return getClient().createSymlink(bucket, symlink, target);
    }

    /**
     * 获取软连接
     */
    public OSSSymlink getSymlink(String bucket, String symlink) {
        return getClient().getSymlink(bucket, symlink);
    }
}
