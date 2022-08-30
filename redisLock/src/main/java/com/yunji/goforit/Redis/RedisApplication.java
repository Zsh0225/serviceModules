package com.yunji.goforit.Redis;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.Jedis;

@EnableDiscoveryClient
@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Bean
    public Redisson redisson(){
        Config config =new Config();
        config.useSingleServer().setAddress("redis://localhost:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);

    }
    @Bean
    public Jedis jedis(){
        Jedis jedis = new Jedis("localhost");
        return jedis;

    }
}
