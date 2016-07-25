package com.rkylin.Controller;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.rkylin.gaterouter.dto.authentication.BankAccountCheckDto;
import com.rkylin.gaterouter.dto.authentication.BankAccountCheckRespDto;
import com.rkylin.gateway.pojo.BusiSysInfo;
import com.rkylin.gateway.utils.BeanUtil;
import com.rkylin.utils.ReferenceConfigCacheFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;

/**
 * Created by 嘉玮 on 2016-6-23.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping(value="/vali",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String auth(BankAccountCheckDto dto, String sysKey, BusiSysInfo bsi) throws Exception {
        System.out.println("收到单笔身份验证请求 start ——————————————————————————————————");
        BankAccountCheckRespDto returnObject = null;
        try {
            dto.setSignMsg(dto.sign(sysKey));

            System.out.println("打印请求Dto："+ BeanUtil.getFieldAndValue(dto));
            System.out.println("打印请求Bsi："+ BeanUtil.getFieldAndValue(bsi));
            System.out.println("打印请求sysKey："+ sysKey);



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
            throw e ;
        }
        System.out.println("收到单笔身份验证请求 end ——————————————————————————————————");
        return "returnCode="+ returnObject.getReturnCode()+"&returnMsg="+returnObject.getReturnMsg()
                +"&channelCode="+returnObject.getChannelCode()+"&channelMsg="+returnObject.getChannelMsg();
    }


}
