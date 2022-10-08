package com.yunji.goforit.OSSPolicy.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/file")
public class UpdateController {
    @Resource
    OSS ossClient;

    @GetMapping("/upload")
    public String file() throws FileNotFoundException {
        String bucketName = "www-zsh0225-top-oss-bucket-test"; // Bucket的名称
        String localFile = "C:\\Users\\ZSH\\Desktop\\photo.jpg";
        String fileKeyName = "photo2.jpg"; // 上传至阿里云的名称，可以使用 / 附带文件夹
        InputStream inputStream = new FileInputStream(localFile);
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileKeyName, inputStream);
        log.info(putObjectResult.getETag());
        return "success";
    }
}
