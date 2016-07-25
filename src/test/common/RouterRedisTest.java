package common;

import org.junit.Test;

/**
 * Created by 嘉玮 on 2016-5-26.
 */
public class RouterRedisTest {

    @Test
    public void testOffline() throws InterruptedException {

        for (int i=0;i<10;i++){

            new Thread(){
                @Override
                public void run() {

                    for (int i=0;i<10;i++){

//                        OfflinePaymentDto paymentDto = new OfflinePaymentDto();
//                        paymentDto.setSignType(1);
//                        paymentDto.setRequestNo("2016042910233001001");
//                        paymentDto.setSysNo("syt0001");
//                        paymentDto.setTransCode("12091");
//                        paymentDto.setBusiCode("12001");
//                        paymentDto.setChannelNo("160611");
//                        paymentDto.setOrgNo("M00000X");
//                        paymentDto.setTransNo("OP20160429102343101");
//                        paymentDto.setPayAmount(2345L);
//                        paymentDto.setCurrency("CNY");
//                        paymentDto.setPayerAccountNo("09876543211234");
//                        paymentDto.setPayerAccountName("ceshi1");
//                //		paymentDto.setReceiverAccountNo("123456789098765432");
//                //		paymentDto.setReceiverAccountName("ceshi2");
//                //		paymentDto.setSubAccountNo("5678909876");
//                //		paymentDto.setSubAccountName("test3");
//                        paymentDto.setBankFlowNo("88886666");
//                        paymentDto.setMobile("18311223344");
//                        paymentDto.setTransTime(new Date());
//                        paymentDto.setPayTime(new Date());
//                        paymentDto.setSignMsg(paymentDto.sign("syt0001"));

                    }

                }
            }.start();

        }


        Thread.sleep(10*1000);
    }
}
