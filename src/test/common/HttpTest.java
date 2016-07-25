package common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by 嘉玮 on 2016-6-16.
 */
public class HttpTest {
    @Test
    public void testHttps(){
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(
                "http://www.11123a66cn.net");
        client.getHttpConnectionManager().getParams()
                .setConnectionTimeout(3000);
        client.getHttpConnectionManager().getParams().setSoTimeout(3000);
        try {
            int statusCode = client.executeMethod(method);
            System.out.println(statusCode);
            byte[] responseBody = null;
            responseBody = method.getResponseBody();
            String result = new String(responseBody);
            System.out.println(result);
        } catch (HttpException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
