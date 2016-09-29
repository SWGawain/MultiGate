package com.rkylin.multigates.Controller;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rkylin.gaterouter.dto.ResponseDto;
import com.rkylin.gaterouter.dto.agentpay.BatchAgentPayDto;
import com.rkylin.gaterouter.dto.agentpay.BatchAgentPayRespDto;
import com.rkylin.gaterouter.dto.agentpay.SingleAgentPayDto;
import com.rkylin.gaterouter.dto.agentpay.SingleAgentPayRespDto;
import com.rkylin.gateway.pojo.BusiSysInfo;
import com.rkylin.gateway.utils.BeanUtil;
import com.rkylin.multigates.utils.ReferenceConfigCacheFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 嘉玮 on 2016-4-28.
 */
@Controller
@RequestMapping("/trade")
public class TradeController {

    /* 批量 */
    @RequestMapping(value = "/batchPay",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String batchPay(BatchAgentPayDto dto,SingleAgentPayDto sap, BusiSysInfo bsi,String sysKey,String idType,String id,String bankCode) {

        BatchAgentPayRespDto respDto = null;
        try {
            respDto = null;

            dto.setBatchNo("Batch"+new Date().getTime());
            dto.setTotalAcount(1);
            dto.setTotalAmount(sap.getPayAmount());
            dto.setCurrency("CNY");

            sap.setTransNo("trans"+new Date().getTime());
            sap.setRemark(sap.getPurpose());
            sap.setSummary(sap.getPurpose());
            Map<String,String> map = Maps.newHashMap();
            map.put("id",id);
            map.put("idtype",idType);
            map.put("bankcode",bankCode);
            sap.setExpand1(new ObjectMapper().writeValueAsString(map));
            sap.setCurrency("CNY");

            System.out.println("打印参数 start ——————————————————————————————————————");
            System.out.println(BeanUtil.getFieldAndValue(dto));
            System.out.println(BeanUtil.getFieldAndValue(bsi));
            System.out.println(BeanUtil.getFieldAndValue(sysKey));

            System.out.println("打印参数 end  ——————————————————————————————————————");
            dto.setSignMsg(dto.sign(sysKey));

         /* 签名操作 */
            dto.setSignMsg(dto.sign(sysKey));

            sap.setSignMsg(sap.sign(sysKey));
            List<SingleAgentPayDto> list = Lists.newArrayList();
            list.add(sap);
            dto.setAgentPayDtoList(list);

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
            Method method = targetInterface.getClass().getMethod(bsi.getDubboMethod(),BatchAgentPayDto.class);
            respDto = (BatchAgentPayRespDto) method.invoke(targetInterface, dto);

            ObjectMapper mapper = new ObjectMapper();
            String ret = mapper.writeValueAsString(respDto);
            System.out.println("返回："+ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getMsg(respDto);
    }

    /* 单笔 */
    @RequestMapping(value = "/singlePay",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String singlePay(SingleAgentPayDto dto,BusiSysInfo bsi,String sysKey,String idType,String id,String bankCode) throws Exception {

        SingleAgentPayRespDto respDto = null;
        try {
            dto.setTransNo("trans"+new Date().getTime());
            Map<String,String> map = Maps.newHashMap();
            map.put("id",id);
            map.put("idtype",idType);
            map.put("bankcode",bankCode);
            dto.setExpand1(new ObjectMapper().writeValueAsString(map));
            dto.setCurrency("CNY");
            dto.setRemark(dto.getPurpose());
            dto.setSummary(dto.getPurpose());


            respDto = null;
            System.out.println("打印参数 start ——————————————————————————————————————");
            System.out.println(BeanUtil.getFieldAndValue(dto));
            System.out.println(BeanUtil.getFieldAndValue(bsi));
            System.out.println(BeanUtil.getFieldAndValue(sysKey));

            System.out.println("打印参数 end  ——————————————————————————————————————");
            dto.setSignMsg(dto.sign(sysKey));

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
            Method method = targetInterface.getClass().getMethod(bsi.getDubboMethod(),SingleAgentPayDto.class);
            respDto = (SingleAgentPayRespDto) method.invoke(targetInterface, dto);

            ObjectMapper mapper = new ObjectMapper();
            String ret = mapper.writeValueAsString(respDto);
            System.out.println("返回："+ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getMsg(respDto);
    }

    /* 获取返回信息 */
    private String getMsg(ResponseDto dto){
        StringBuilder builder = new StringBuilder();
        builder.append("returnCode="+dto.getReturnCode()+"&returnMsg="+dto.getReturnMsg());
        if(dto instanceof SingleAgentPayRespDto){
            builder.append("&channelCode="+dto.getChannelCode()+"&channelMsg="+dto.getChannelMsg());
        }

        return builder.toString();
    }

}
