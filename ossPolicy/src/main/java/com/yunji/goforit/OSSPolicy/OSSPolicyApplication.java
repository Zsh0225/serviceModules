package com.yunji.goforit.OSSPolicy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * @author ZSH
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OSSPolicyApplication {
    public static void main(String[] args) {
        SpringApplication.run(OSSPolicyApplication.class,args); }
}
