package com.rkylin.Controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.rkylin.gaterouter.dto.ResponseDto;
import com.rkylin.gaterouter.dto.agentpay.BatchAgentPayDto;
import com.rkylin.gaterouter.dto.agentpay.BatchAgentPayRespDto;
import com.rkylin.gaterouter.dto.agentpay.SingleAgentPayDto;
import com.rkylin.gaterouter.dto.agentpay.SingleAgentPayRespDto;
import com.rkylin.gateway.pojo.BusiSysInfo;
import com.rkylin.gateway.utils.BeanUtil;
import com.rkylin.utils.DubboUtil2;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
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
    public String batchPay(BatchAgentPayDto dto,SingleAgentPayDto sap, BusiSysInfo bsi,String sysKey) throws Exception {

        BatchAgentPayRespDto respDto = null;

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

        /*  发送 */
        bsi.setSysNo(null);
        try {
            respDto = (BatchAgentPayRespDto) DubboUtil2.sendMessage(bsi, dto, BatchAgentPayDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println(BeanUtil.getFieldAndValue(respDto));

        return getMsg(respDto);
    }

    /* 单笔 */
    @RequestMapping(value = "/singlePay",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String singlePay(SingleAgentPayDto dto,BusiSysInfo bsi,String sysKey) throws Exception {

        SingleAgentPayRespDto respDto = null;
        System.out.println("打印参数 start ——————————————————————————————————————");
        System.out.println(BeanUtil.getFieldAndValue(dto));
        System.out.println(BeanUtil.getFieldAndValue(bsi));
        System.out.println(BeanUtil.getFieldAndValue(sysKey));

        System.out.println("打印参数 end  ——————————————————————————————————————");
        dto.setSignMsg(dto.sign(sysKey));

         /*  发送 */
        System.out.println(bsi.getDubboUrl());
        bsi.setSysNo(null);
        try {
            respDto = (SingleAgentPayRespDto) DubboUtil2.sendMessage(bsi, dto, SingleAgentPayDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw  e ;
        }
        System.out.println(BeanUtil.getFieldAndValue(respDto));

        return getMsg(respDto);
    }

    /* 保存临时数据 */
    @RequestMapping(value = "/saveTemp",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String saveTemp(String key, String value, HttpSession session){

        try {
            Map<String,String> temp = (Map<String, String>) session.getAttribute("temp");
            if(temp == null){
                temp = new HashMap<String, String>();
                session.setAttribute("temp",temp);
            }
            temp.put(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /* 获取临时数据 */
    @RequestMapping(value = "/getTemp",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String getTemp(String key,HttpSession session){

        Map<String,String> temp = (Map<String, String>) session.getAttribute("temp");

        String ret ="";

        if(temp==null){
            temp = new HashMap<String, String>();
        }
        try {

            if(!Strings.isNullOrEmpty(key)){
                String value = temp.get(key);
                if(!Strings.isNullOrEmpty(value)){
                    return value ;
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            ret = mapper.writeValueAsString(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
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
