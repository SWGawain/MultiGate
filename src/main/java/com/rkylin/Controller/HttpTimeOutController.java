package com.rkylin.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 嘉玮 on 2016-6-1.
 */
@Controller
@RequestMapping("/timeout")
public class HttpTimeOutController {

    @RequestMapping
    @ResponseBody
    public String hello() throws InterruptedException {
        System.out.println("Request come in  ...");
        Thread.sleep(1000*60);

        System.out.println("Thread sleep over");
        return "hello";
    }
}
