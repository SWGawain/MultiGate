package com.rkylin.multigates.Controller;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.rkylin.gaterouter.dto.RequestDto;
import com.rkylin.gaterouter.dto.authentication.BankAccountCheckDto;
import com.rkylin.gaterouter.dto.authentication.BankAccountCheckRespDto;
import com.rkylin.gateway.pojo.BusiSysInfo;
import com.rkylin.multigates.utils.ReferenceConfigCacheFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;

/**
 * Created by 嘉玮 on 2016-7-19.
 */
@Controller
@RequestMapping("/release")
public class ReleaseController {


    @RequestMapping(value="/ll",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String release(RequestDto dto ,BusiSysInfo bsi, String sysKey){


        Object returnObject ;
        try {
            ApplicationConfig application = new ApplicationConfig();
            application.setName("DubboMsgCenter");
            ReferenceConfig reference = new ReferenceConfig(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
            reference.setApplication(application);
//        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
            reference.setInterface(bsi.getDubboApi());
            reference.setUrl(bsi.getDubboUrl());
            reference.setGroup(bsi.getDubboGroup());
            reference.setVersion(bsi.getDubboVersion());
            reference.setRetries(0);
            reference.setTimeout(120000);

            Object targetInterface = ReferenceConfigCacheFactory.get().get(reference);
            Method method = targetInterface.getClass().getMethod(bsi.getDubboMethod(),BankAccountCheckDto.class);
            returnObject = (BankAccountCheckRespDto) method.invoke(targetInterface, dto);

            ObjectMapper mapper = new ObjectMapper();
            String ret = mapper.writeValueAsString(returnObject);
            System.out.println("返回："+ret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
