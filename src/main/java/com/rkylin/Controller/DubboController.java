package com.rkylin.Controller;

import com.google.common.collect.Maps;
import com.rkylin.gaterouter.dto.Dto;
import com.rkylin.gaterouter.dto.agentpay.*;
import com.rkylin.gateway.MessageCenter.utils.DubboUtil;
import com.rkylin.gateway.pojo.BusiSysInfo;
import com.rkylin.gateway.utils.RkylinBeanUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by 嘉玮 on 2015-12-10.
 */
@Controller
@RequestMapping("/dubbo")
public class DubboController {

    //路由代收付dubbo配置
    private static String[] routerAgentConfig = new String[]{
            "com.rkylin.gaterouter.service.AgentPaymentService", "gaterouter"
    };
    //通联代收付dubbo配置
    private static String[] TLAgentConfig = new String[]{
            "com.rkylin.gateway.service.AgentPayAcpService", "TongLian_AgentPay"
    };
    //联动代收付dubbo配置
    private static String[] umpAgentConfig = new String[]{
            "com.rkylin.gateway.service.AgentPayAcpService","UMP_AgentPay"
    };
    //畅捷支付dubbo配置
    private static String[] cjAgentConfig = new String[]{
            "com.rkylin.gateway.service.AgentPayAcpService","ChanJie_AgentPay"
    };

    //配置map
    private Map<String, String[]> configMap = Maps.newHashMap();

    {
        configMap.put("TongLian_AgentPay", TLAgentConfig);
        configMap.put("gaterouter", routerAgentConfig);
        configMap.put("UMP_AgentPay",umpAgentConfig);
        configMap.put("ChanJie_AgentPay",cjAgentConfig);
    }

    /**
     * 发送单笔代付交易
     */
    @RequestMapping(value = "/singlePay", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String singlePay(SingleAgentPayDto dto, String sysKey ,String channel_mark, String url) throws Exception {
        dto.setSignMsg(dto.sign(sysKey));
        //打印参数
        Map dtoMap = RkylinBeanUtil.convertBean2Map(dto);
        for (Object mkey : dtoMap.keySet()){
            System.out.println(mkey+" : "+dtoMap.get(mkey));
        }
        System.out.println("Key : "+sysKey);
        System.out.println("channel_mark : " + channel_mark);
        System.out.println("url : " + url);

        SingleAgentPayRespDto respDto = (SingleAgentPayRespDto) DubboUtil.sendMessage(
                createBsi(configMap.get(channel_mark), new String[]{
                        channel_mark.equals("gaterouter")?"singleAgentPay" : "singlePayExecute", url
                }), dto, SingleAgentPayDto.class);
//        SingleAgentPayRespDto respDto = new SingleAgentPayRespDto();
        //转换dto
        return createTable(respDto);
    }

    /**
     * 发送单笔代收交易
     */
    @RequestMapping(value = "/singleAcp", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String singleAcp(SingleAgentAcpRespDto dto, String sysKey ,String channel_mark, String url) throws Exception {
        dto.setSignMsg(dto.sign(sysKey));
        //打印参数
        Map dtoMap = RkylinBeanUtil.convertBean2Map(dto);
        for (Object mkey : dtoMap.keySet()){
            System.out.println(mkey+" : "+dtoMap.get(mkey));
        }
        System.out.println("Key : "+sysKey);
        System.out.println("channel_mark : " + channel_mark);
        System.out.println("url : " + url);

        SingleAgentPayRespDto respDto = (SingleAgentPayRespDto) DubboUtil.sendMessage(
                createBsi(configMap.get(channel_mark), new String[]{
                        channel_mark.equals("gaterouter")?"singleAgentAcp":"singleAcpExecute", url
                }), dto, SingleAgentPayDto.class);
//        SingleAgentAcpRespDto respDto = new SingleAgentAcpRespDto();
        //转换dto
        return createTable(respDto);
    }

    /**
     * 发送批量代付
     * @param dto
     * @param sysKey
     * @param channel_mark
     * @param url
     * @return
     */
    @RequestMapping(value = "/batchPay" ,produces = "text/html;charset=utf-8")
    @ResponseBody
    public String batchPay(BatchAgentPayDto dto,String sysKey,String channel_mark,String url) throws Exception {
        dto.setSignMsg(dto.sign(sysKey));
        List<SingleAgentPayDto> lists = dto.getAgentPayDtoList();
        for (SingleAgentPayDto listDto : lists){
            listDto.setSignMsg(listDto.sign(sysKey));
        }

        BatchAgentPayRespDto respDto = (BatchAgentPayRespDto) DubboUtil.sendMessage(
                createBsi(configMap.get(channel_mark), new String[]{
                        channel_mark.equals("gaterouter")?"batchAgentPay" : "batchPayExecute", url
                }), dto, BatchAgentPayDto.class);
//        SingleAgentPayRespDto respDto = new SingleAgentPayRespDto();
        //转换dto
        return createTable(respDto,respDto.getSingleAgentPayRespDtoList());
    }
    /**
     * 发送批量代收
     * @param dto
     * @param sysKey
     * @param channel_mark
     * @param url
     * @return
     */
    @RequestMapping(value = "/batchAcp" ,produces = "text/html;charset=utf-8")
    @ResponseBody
    public String batchAcp(BatchAgentAcpDto dto,String sysKey,String channel_mark ,String url) throws Exception {
        dto.setSignMsg(dto.sign(sysKey));
        List<SingleAgentAcpDto> lists = dto.getAgentAcpDtoList();
        for (SingleAgentAcpDto listDto : lists){
            listDto.setSignMsg(listDto.sign(sysKey));
        }

        BatchAgentAcpRespDto respDto = (BatchAgentAcpRespDto) DubboUtil.sendMessage(
                createBsi(configMap.get(channel_mark), new String[]{
                        channel_mark.equals("gaterouter")?"batchAgentAcp" : "batchAcpExecute", url
                }), dto, BatchAgentAcpDto.class);
//        SingleAgentPayRespDto respDto = new SingleAgentPayRespDto();
        //转换dto
        return createTable(respDto,respDto.getSingleAgentAcpRespDtoList());
    }


    /**
     * ——————————————————******工具方法******————————————————————————————
     */

    /**
     * 把dto转换成html-table代码
     * @param dto
     * @param dtos
     * @return
     */
    public String createTable(Dto dto,List dtos){
        String tableStr = "</table>";
        StringBuilder builder = new StringBuilder();
        builder.append(createTable(dto));
        builder.delete(builder.length() - tableStr.length(), builder.length());

        StringBuilder listBuilder = new StringBuilder();
        for (Object listDto : dtos){
            listBuilder.append(createTable((Dto)listDto));
        }

        builder.append("<tr>" +
                "<td> List </td>" +
                "<td>"+listBuilder.toString()+"</td>" +
                "</tr>");
        builder.append(tableStr);
        return builder.toString();
    }

    /**
     * 把dto转换成html-table代码
     * @param dto
     * @return
     */
    public String createTable(Dto dto){
        Map map =  RkylinBeanUtil.convertBean2Map(dto);
        StringBuilder builder = new StringBuilder();
        builder.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\">" +
                "<tr>" +
                "<td width=\"100\">参数名</td>" +
                "<td width=\"100\">值</td>" +
                "</tr>");
        for (Object key : map.keySet()){
            builder.append("<tr>" +
                    "<td>"+key+"</td>" +
                    "<td>"+map.get(key)+"</td>" +
                    "</tr>");
        }
        builder.append("</table>");
        return builder.toString();
    }
    /**
     * 构造BusiSysInfo
     * @param config_1
     * @param config_2
     * @return
     */
    public BusiSysInfo createBsi(String[] config_1, String[] config_2) {
        BusiSysInfo bsi = new BusiSysInfo();
        bsi.setDubboApi(config_1[0]);
        bsi.setDubboGroup(config_1[1]);
        bsi.setDubboMethod(config_2[0]);
        bsi.setDubboUrl(config_2[1]);
        bsi.setDubboVersion("1.0.0");
        return bsi;
    }
}
