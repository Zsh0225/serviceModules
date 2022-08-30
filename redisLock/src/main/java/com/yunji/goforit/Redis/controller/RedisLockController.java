package com.yunji.goforit.Redis.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * @author ZSH
 */
@RestController
public class RedisLockController {

    @Autowired
    private Redisson redisson;
    @Autowired
    Jedis jedis;
    @GetMapping("/test")

    public void testcon(){
        Jedis jedis =new Jedis("localhost");
        System.out.println(jedis.ping());
    }

    @GetMapping("decsr")
    public String descstock(){
        String lockkey = "lock:101";
        RLock rLock = redisson.getLock(lockkey);
        rLock.lock();
        try {
            int stock = Integer.parseInt(jedis.get("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                jedis.set("stock", realStock + "");
                System.out.println("扣除成功，剩余库存" + realStock);
            } else {
                System.out.println("扣除失败，库存不足");
            }
        }
            finally{
                rLock.unlock();
            }
        return  "end";
    }
}
