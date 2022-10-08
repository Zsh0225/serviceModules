package com.yunji.goforit.OSSPolicy.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.sun.javafx.binding.StringFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Date;

@Slf4j
@CrossOrigin
@RestController
public class PolicyController {
    @Resource
    OSS ossClient;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String accessKey;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucketName}")
    private String bucketName;


    @GetMapping("/getPolicy")
    public JSONObject getPolicy() throws UnsupportedEncodingException, JSONException {
        // host的格式为 bucketname.endpoint
        String host = StringFormatter.concat("https://", bucketName, ".", endpoint).getValue();
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        // String callbackUrl = "http://88.88.88.88:8888";
        // 每一天产生一个文件夹
        String dir = LocalDate.now().toString() + "/"; // 用户上传文件时指定的前缀,如果是 / 则自动检测为文件夹。

        JSONObject jsonObject = new JSONObject();

        long expireTime = 100;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000; //过期时间 100 秒
        Date expiration = new Date(expireEndTime);
        // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes("utf-8");
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        jsonObject.put("OSSAccessKeyId", accessId);
        jsonObject.put("policy", encodedPolicy);
        jsonObject.put("signature", postSignature);
        jsonObject.put("dir", dir);
        jsonObject.put("host", host);
        jsonObject.put("expire", String.valueOf(expireEndTime / 1000));
        return jsonObject;
    }
}
