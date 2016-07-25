package com.rkylin.Controller;

import com.rkylin.bean.UserModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 嘉玮 on 2015-12-11.
 */
@Controller
@RequestMapping("/list")
public class ListController {

    @RequestMapping
    public void testList(UserModel um){
        System.out.println(um.getUsers().size());
    }

//    @Autowired
//    RedisTemplate redisTemplate;
//
//    @RequestMapping("/redisTest")
//    public String testRedis(){
//
//        Long size = (Long) redisTemplate.execute(new RedisCallback() {
//            @Override
//            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                return redisConnection.dbSize();
//            }
//        });
//        return size+"";
//    }
}
