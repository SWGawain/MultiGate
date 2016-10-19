package com.rkylin.multigates.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rkylin.gaterouter.dto.agentpay.BatchAgentAcpDto;
import com.rkylin.gaterouter.dto.agentpay.SingleAgentAcpDto;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 嘉玮 on 2016-10-17.
 */
@Service
public class BatchInfos {

    public BatchAgentAcpDto parseExcel(File file,long limitAmount){
        Workbook wb = null;
        try {
            if (file.getName().indexOf("xlsx")!=-1) {
                wb = new XSSFWorkbook(new FileInputStream(file));
            } else {
                wb = new HSSFWorkbook(new FileInputStream(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        if(rows <= 1){
            throw new RuntimeException("未检测到代扣信息");
        }

        BatchAgentAcpDto dto = new BatchAgentAcpDto();
        dto.setRequestNo("req"+new Date().getTime());
        dto.setTransCode("11022");
        dto.setBusiCode("09100");  //业务代码 10100：保险分红
        dto.setSysNo("dsfxt001");   //系统编号  0078
        dto.setChannelNo("110104");  //渠道编号  10111
        String batchNo = "90"+System.currentTimeMillis();    //maythegodbewithme01
        dto.setBatchNo(batchNo);     //批次号

        List<SingleAgentAcpDto> agentAcpDtoList = new ArrayList<SingleAgentAcpDto>();
        long totalAmount = 0;
        for (int r = 1 ; r < rows ; r ++){
            Row row = sheet.getRow(r);

            List<SingleAgentAcpDto> saas = createSaa(row, batchNo + r, limitAmount);

            DataFormatter formatter = new DataFormatter();
            String amount =formatter.formatCellValue(row.getCell(1));
            totalAmount += Long.parseLong(amount);

            agentAcpDtoList.addAll(saas);
        }

        dto.setAgentAcpDtoList(agentAcpDtoList);
        dto.setTotalAcount(agentAcpDtoList.size());
        dto.setTotalAmount(totalAmount);
        dto.setCurrency("CNY");

        dto.setSignType(1);

        return dto ;
    }


    private List<SingleAgentAcpDto> createSaa(Row row,String transNo,long limitAmount){
        List<SingleAgentAcpDto> list = Lists.newArrayList();
        DataFormatter formatter = new DataFormatter();
        String amount =formatter.formatCellValue(row.getCell(1));
        long pay_amount = Long.parseLong(amount);
        long nums = pay_amount / limitAmount ;
        long lastAmount = pay_amount % limitAmount ;

        for (int i = 0; i<= nums ; i ++){
            SingleAgentAcpDto saa = new SingleAgentAcpDto();

            saa.setRequestNo("req"+new Date().getTime());
            saa.setTransCode("11022");
            saa.setBusiCode("09100");// 代发奖金
            saa.setTransNo(transNo+i);
            saa.setSysNo("dsfxt001");
            saa.setChannelNo("110104");
            saa.setCurrency("CNY");

            String accountName = formatter.formatCellValue(row.getCell(0));

            String accountNo = formatter.formatCellValue(row.getCell(2));
            String bankNo = formatter.formatCellValue(row.getCell(3));
            String bankName = formatter.formatCellValue(row.getCell(4));
            String idtype = formatter.formatCellValue(row.getCell(5));
            String id = formatter.formatCellValue(row.getCell(6));
            String accountType = formatter.formatCellValue(row.getCell(7));
            String remark = formatter.formatCellValue(row.getCell(8));

            if(i!=nums){
                saa.setPayAmount(limitAmount);
            }else{
                saa.setPayAmount(lastAmount);
                if(lastAmount==0){
                    continue;
                }
            }



            saa.setPayerAccountName(accountName);
            saa.setPayerAccountNo(accountNo);
            saa.setPayerBankNo(bankNo);
            saa.setPayerBankName(bankName);
            saa.setPayerAccountType(Strings.isNullOrEmpty(accountType)?10:Integer.parseInt(accountType));
            if(!Strings.isNullOrEmpty(id) && !Strings.isNullOrEmpty(idtype)){
                Map ex_param = Maps.newHashMap();
                ex_param.put("id",id);
                ex_param.put("idtype",idtype);
                try {
                    saa.setExpand1(new ObjectMapper().writeValueAsString(ex_param));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            saa.setRemark(remark);
            saa.setSummary(remark);
            saa.setPurpose(remark);

            saa.setSignType(1);

            list.add(saa);
        }

        return list ;
    }
}
