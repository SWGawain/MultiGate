package com.rkylin.multigates.Controller;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rkylin.gaterouter.dto.ResponseDto;
import com.rkylin.gaterouter.dto.agentpay.*;
import com.rkylin.gateway.pojo.BusiSysInfo;
import com.rkylin.gateway.utils.BeanUtil;
import com.rkylin.multigates.service.BatchInfos;
import com.rkylin.multigates.utils.PrintBean;
import com.rkylin.multigates.utils.ReferenceConfigCacheFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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

            sap.setTransNo(""+new Date().getTime());
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
            dto.setTransNo(new Date().getTime()+"");
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

    /**
     * *****************************************************************************************************************
     */

    /**
     * 代收相关
     */

    @Autowired
    BatchInfos batchInfos ;

    /* 批量代收 */
    @RequestMapping(value = "/batchAcp/upload",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String batchAcp(@RequestParam(value = "file", required = false) MultipartFile file,
                           HttpServletRequest request,String limitAmount) throws IOException {
        if(file == null){
            return "{error:'illegal data，please check again'}";
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        fileName = fileName.substring(0,fileName.indexOf(".xls"))+ "_" + new Date().getTime() + fileName.substring(fileName.indexOf(".xls"));
//        String fileName = new Date().getTime()+".jpg";
        System.out.println(path);
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }

        //保存
        try {
            file.transferTo(targetFile);
            targetFile.deleteOnExit();

            System.out.println("单笔拆分金额："+limitAmount);
            long amount = 500000 ;
            if(!Strings.isNullOrEmpty(limitAmount)){
                limitAmount = limitAmount.replaceAll(",","").replaceAll("\\.","");
                amount = Long.parseLong(limitAmount);
            }
            BatchAgentAcpDto batchAgentAcpDto = batchInfos.parseExcel(targetFile,amount);

            PrintBean.print(batchAgentAcpDto);
            String retMsg = new ObjectMapper().writeValueAsString(batchAgentAcpDto);

            return retMsg;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return  "{error:"+e.getMessage()+"}";
        }
    }

     /* 批量代收 */
    @RequestMapping(value = "/batchAcp/send",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String batchAcp(String json,String dubboUrl,String orgNo) throws IOException {

        System.out.println("接收到代扣请求，url:"+dubboUrl);
        BatchAgentAcpDto dto = new ObjectMapper().readValue(json, BatchAgentAcpDto.class);
        dto.setOrgNo(orgNo);
        for (SingleAgentAcpDto saa : dto.getAgentAcpDtoList()) {
            saa.setOrgNo(orgNo);
            saa.setSignMsg(saa.sign("dsfxt0001"));
        }
        dto.setSignMsg(dto.sign("dsfxt0001"));

        BatchAgentAcpRespDto respDto = null ;
        try {
            ApplicationConfig application = new ApplicationConfig();
            application.setName("DubboMsgCenter");
            ReferenceConfig reference = new ReferenceConfig(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
            reference.setApplication(application);
//        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
            reference.setInterface("com.rkylin.gaterouter.service.AgentPaymentService");
            reference.setUrl("dubbo://"+dubboUrl);
            reference.setGroup("gaterouter");
            reference.setVersion("1.0.0");
            reference.setRetries(0);
            reference.setTimeout(120000);

            Object targetInterface = ReferenceConfigCacheFactory.get().get(reference);
            Method method = targetInterface.getClass().getMethod("batchAgentAcp",BatchAgentAcpDto.class);
            respDto = (BatchAgentAcpRespDto) method.invoke(targetInterface, dto);

            ObjectMapper mapper = new ObjectMapper();
            String ret = mapper.writeValueAsString(respDto);
            System.out.println("返回："+ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getMsg(respDto);
    }
}
