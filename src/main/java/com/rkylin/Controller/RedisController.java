package com.rkylin.Controller;

import com.google.common.collect.Maps;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 嘉玮 on 2016-4-1.
 */
@Controller
@RequestMapping("/redis")
public class RedisController {

    /*
    * ——————————————————————————————————————————————————————
    * 初始方法
    * ——————————————————————————————————————————————————————
    * */

    /**
     * 建立连接方法
     * @param url   地址
     * @param pass  密码
     * @param port  端口
     * @return 成功与否
     */
    @RequestMapping(value = "/init",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String init(String url,String pass,String port,HttpSession session){
        System.out.println("Init Connection start ——————————————————————————");
        createJedis(url, pass, port, session);
        System.out.println("Init Connection end   ——————————————————————————");
        return "success";
    }

    public void createJedis(String host,String pass,String port,HttpSession session){
        Map<String,String> param = Maps.newHashMap();
        param.put("url",host);
        param.put("pass",pass);
        param.put("port",port);

        System.out.println("Host:"+host);
        System.out.println("Pass:"+pass);
        System.out.println("Port:"+port);
        Jedis jedis = new Jedis(host,Integer.parseInt(port));
        jedis.auth(pass);

        session.setAttribute("jedis",jedis);
        session.setAttribute("infos",param);
    }

    /**
     * 断开连接方法
     * @return
     */
    @RequestMapping(value = "/quit",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String quit(HttpSession session){
        System.out.println("Release Connection start ——————————————————————————");
        Map<String,String> infos = (Map<String, String>) session.getAttribute("infos");
        System.out.println("Connection Info : url="+infos.get("url")+" pass="+infos.get("pass") + " port="+infos.get("port"));
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        jedis.quit();
        session.removeAttribute("jedis");
        session.removeAttribute("infos");

        System.out.println("Release Connection end   ——————————————————————————");
        return "success";
    }

    /**
     * 重新连接方法
     * @return
     */
    @RequestMapping(value = "/reconnect",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String reconnect(HttpSession session){
        System.out.println("Reconnect start ——————————————————————————");
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        jedis.quit();
        Map<String,String> infos = (Map<String, String>) session.getAttribute("infos");
        createJedis(infos.get("url"), infos.get("pass"), infos.get("port"),session);
        System.out.println("Connection Info : url="+infos.get("url")+" pass="+infos.get("pass") + " port="+infos.get("port"));
        System.out.println("Reconnect end   ——————————————————————————");
        return "success";
    }

    /**
     * 查询session中的链接
     * @return
     */
    @RequestMapping(value = "/qconnect",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String qconnect(HttpSession session){
        System.out.println("Query Cconnect start ——————————————————————————");
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        if(jedis==null){
            return "null";
        }

        Map<String,String> infos = (Map<String, String>) session.getAttribute("infos");
        System.out.println("Connection Info : url="+infos.get("url")+" pass="+infos.get("pass") + " port="+infos.get("port"));
        System.out.println("Query Cconnect end   ——————————————————————————");
        return infos.get("url");
    }

    /*
    * ——————————————————————————————————————————————————————
    * 常用方法 part1
    * ——————————————————————————————————————————————————————
    * */

    /**
     * 查询所有的key
     * @param key
     * @return
     */
    @RequestMapping(value = "/search",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String search(String key,HttpSession session) {
        System.out.println("Search for keys :"+key + "  start ——————————————————————");
        Set<String> keys;
        String json;
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            keys = jedis.keys("*" + key + "*");
            ObjectMapper mapper = new ObjectMapper();
            Map<String,String> map = new HashMap<String, String>();
            for (String k : keys ){
                String hc = k.hashCode()+"";
                map.put(hc,k);
            }
            session.setAttribute("map",map);
            json = mapper.writeValueAsString(map);
            System.out.println("Key:" + key + " result:" + json);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("Search for keys :"+key + "  end   ——————————————————————");
        return json;
    }

    /**
     * 获取值
     * @param key
     * @return
     */
    @RequestMapping(value = "/getValue",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String getValue(String key,HttpSession session){
        System.out.println("Get for key :"+key + "  start ——————————————————————");
        String value = null;
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            value = jedis.get(key);
            System.out.println("value:"+value);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("Get for key :"+key + "  end   ——————————————————————");
        return value;
    }

    /**
     * 修改值
     * @param key
     * @return
     */
    @RequestMapping(value = "/modiValue",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String modiValue(String key,String value,HttpSession session){
        System.out.println("Modify for key :"+key + "  start ——————————————————————");
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            System.out.println("value:"+value);
            jedis.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("Modify for key :"+key + "  end   ——————————————————————");
        return "success";
    }

    /**
     * 获取过期时间
     * @param key
     * @return
     */
    @RequestMapping(value = "/ttl",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String ttl(String key,HttpSession session) {
        System.out.println("TTL for key :"+key + "  end   ——————————————————————");
        Long ttl;
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            ttl = jedis.ttl(key);
            System.out.println("TTL:"+ttl);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("TTL for key :"+key + "  end   ——————————————————————");
        return ttl+"";
    }

    /**
     * 设置过期时间
     * @param key
     * @return
     */
    @RequestMapping(value = "/expire",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String expire(String key,String value,HttpSession session) {
        System.out.println("TTL for key :"+key + "  start ——————————————————————");
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            System.out.println("TIME:"+value);
            jedis.expire(key,Integer.parseInt(value));
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("TTL for key :"+key + "  end   ——————————————————————");
        return "success";
    }

    /**
     * 删除
     * @param key
     * @return
     */
    @RequestMapping(value = "/del",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String del(String key,HttpSession session) {
        System.out.println("DEL for key :"+key + "  start ——————————————————————");
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            Long del = jedis.del(key);
            System.out.println("DEL for :"+del);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("DEL for key :"+key + "  end   ——————————————————————");
        return "success";
    }


    /*
    * ——————————————————————————————————————————————————————
    * 常用方法 part2
    * ——————————————————————————————————————————————————————
    * */

    /**
     * 查询总数
     * @return
     */
    @RequestMapping(value = "/size",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String size(HttpSession session) {
        System.out.println("Query DBsize  start ——————————————————————");
        Long aLong;
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            aLong = jedis.dbSize();
            System.out.println("SIZE :"+aLong);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("Query DBsize  end   ——————————————————————");
        return aLong+"";
    }

    /**
     * 清空当前库表
     * @return
     */
    @RequestMapping(value = "/flush",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String flush(HttpSession session) {
        System.out.println("FlushDB  start ——————————————————————");
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            String result = jedis.flushDB();
            System.out.println("RESULT :"+result);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("FlushDB  end   ——————————————————————");
        return "success";
    }

    /**
     * 清理当前页所有key
     */
    @RequestMapping(value = "/clear",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String clear(@RequestParam("keys[]") List<String> keys, HttpSession session) {
        System.out.println("ClearPage  start ——————————————————————");
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            if(keys == null || keys.size()==0) throw new RuntimeException("无参数");
            Map<String,String> map = (Map<String, String>) session.getAttribute("map");
            for (String hck : keys){
                System.out.println(hck);
                String key = map.get(hck);
                System.out.println(key);
                Long del = jedis.del(key);
                System.out.println("RESULT :"+del);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("ClearPage  end   ——————————————————————");
        return "success";
    }

    /**
     * 切换数据库
     */
    @RequestMapping(value = "/select",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String select(int database, HttpSession session) {
        System.out.println("SelectDB  start ——————————————————————");
        Jedis jedis = (Jedis) session.getAttribute("jedis");
        try {
            System.out.println("切换到"+database+"库");
           jedis.select(database);
            System.out.println("切换成功");
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        System.out.println("SelectDB  end   ——————————————————————");
        return "success";
    }
}
