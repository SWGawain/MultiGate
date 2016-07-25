package db;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rkylin.utils.JdbcUtils;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by 嘉玮 on 2016-6-14.
 */
public class DevDBTest {

    @Test
    public void testDB() throws SQLException {
        JdbcUtils utils = new JdbcUtils();
        utils.getConnection();

        Map<String, Object> result = utils.findSimpleResult("select * from UMP_BANK_CODE where BANK_ID=1", null);

        for (Map.Entry<String,Object> entry : result.entrySet()){
            System.out.print("Key:"+entry.getKey());
            System.out.println("Value:"+entry.getValue());
        }

        utils.releaseConn();
    }

    @Test
    public void ump() throws SQLException {
        JdbcUtils utils = new JdbcUtils();
        utils.getConnection();

        String sql_query = "SELECT * FROM UMP_BANK_CODE";
        String sql_update = "UPDATE UMP_BANK_CODE SET BANK_SNUM=? WHERE BANK_ID=?";

        List<Map<String, Object>> results = utils.findMoreResult(sql_query, null);
        for (Map<String,Object> result : results){
            System.out.println(result.get("BANK_ID")+" "+result.get("BANK_NAME")+" "+result.get("BANK_CODE"));
            String bank_num = (String) result.get("BANK_NUM");
            if(!Strings.isNullOrEmpty(bank_num)){
                System.out.println("BANK_NUM不为空，"+bank_num+" BANK_SNAME:"+bank_num.substring(0,3));

                List<Object> params = Lists.newArrayList();
                params.add(bank_num.substring(0,3));
                params.add(result.get("BANK_ID"));
                boolean b = utils.updateByPreparedStatement(sql_update, params);
                System.out.println("更新完成 "+ (b?"true":"false########################"));
            }else{
                System.out.println("BANK_NUM为空*****************");
            }
        }

        utils.releaseConn();
    }

    @Test
    public void buildProvinceAndCity() throws SQLException {
        JdbcUtils utils = new JdbcUtils();
        utils.getConnection();

        String sql_queryUMPBankCode = "SELECT * FROM UMP_BANK_CODE";
        String sql_update = "UPDATE UMP_BANK_CODE SET PROVINCE=?,CITY=? WHERE BANK_ID=?";
        String sql_queryOpenBankCode = "SELECT * FROM OPEN_BANK_CODE WHERE PAY_BANK_CODE=?";

        List<Map<String, Object>> results = utils.findMoreResult(sql_queryUMPBankCode, null);
        for (Map<String,Object> result : results){
            String bank_id = result.get("BANK_ID")+"";
            Object bank_num = result.get("BANK_NUM");
            System.out.println(result.get("BANK_ID")+" "+result.get("BANK_NAME")+" "+result.get("BANK_CODE"));

            if(!Strings.isNullOrEmpty(bank_num+"")){
                List<Object> params = Lists.newArrayList();
                params.add(bank_num);
                Map<String, Object> openbank = utils.findSimpleResult(sql_queryOpenBankCode, params);
                Object city_name = openbank.get("CITY_NAME");
                Object province_name = openbank.get("PROVINCE_NAME");
                System.out.println(city_name +" "+ province_name);

                List<Object> params_update = Lists.newArrayList();
                params_update.add(province_name);
                params_update.add(city_name);
                params_update.add(bank_id);

                utils.updateByPreparedStatement(sql_update,params_update);
            }
        }


        utils.releaseConn();
    }

    @Test
    public void check() throws SQLException {
        JdbcUtils utils = new JdbcUtils();
        utils.getConnection();

        String sql = "SELECT * FROM UMP_BANK_CODE";
        List<Map<String, Object>> results = utils.findMoreResult(sql, null);

        Set<String> keys = Sets.newHashSet();
        for (Map<String,Object> result : results){
            Object snum = result.get("BANK_SNUM");
            Object city = result.get("CITY");
            Object province = result.get("PROVINCE");

            String snum_Str = Strings.isNullOrEmpty(snum+"")? "null":snum+"";
            String city_Str = Strings.isNullOrEmpty(city+"")? "null":city+"";
            String province_Str = Strings.isNullOrEmpty(province+"")? "null":province+"";

            String key = snum_Str+"_"+city_Str+"_"+province_Str;
            System.out.println("Bank_Key:"+key);
            boolean contains = keys.contains(key);
            if(contains){
                System.out.println("***************************");
                for (Map.Entry<String,Object> entry : result.entrySet()){
                    System.out.print("Key:"+entry.getKey()+"_");
                    System.out.println("Value:"+entry.getValue());
                }
                System.out.println("------------------------------");
                continue;
            }

            System.out.println("不包含此Key");
            keys.add(key);
        }


        utils.releaseConn();
    }

    @Test
    public void delProvinceAndCity() throws SQLException {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql_query = "SELECT * FROM UMP_BANK_CODE";
        String sql_update = "UPDATE UMP_BANK_CODE SET CITY=?,PROVINCE=? WHERE BANK_ID=?";

        List<Map<String, Object>> results = jdbcUtils.findMoreResult(sql_query, null);
        for (Map<String,Object> result : results){
            for (Map.Entry<String,Object> entry:result.entrySet()){
                System.out.print(entry.getKey()+":");
                System.out.print(entry.getValue()+" ");
            }
            System.out.println();

            String bank_id = result.get("BANK_ID")+"";
            String city = result.get("CITY").toString().replace("市","").replace("省","");
            String province = result.get("PROVINCE").toString().replace("市","").replace("省","");

            List<Object> params = Lists.newArrayList();
            params.add(city);
            params.add(province);
            params.add(bank_id);

            boolean b = jdbcUtils.updateByPreparedStatement(sql_update, params);
            System.out.println("更新完成："+ (b?"TRUE":"FAIL******************"));
        }

        jdbcUtils.releaseConn();
    }


    @Test
    public void fill0() throws SQLException {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql_query = "SELECT * FROM UMP_BANK_CODE";
        String sql_update = "UPDATE UMP_BANK_CODE SET BANK_SNUM=? WHERE BANK_ID=?";
        List<Map<String, Object>> results = jdbcUtils.findMoreResult(sql_query, null);
        for (Map<String,Object> result : results){
            for (Map.Entry<String,Object> entry:result.entrySet()){
                System.out.print(entry.getKey()+":");
                System.out.print(entry.getValue()+" ");
            }
            System.out.println();

            String bank_id = result.get("BANK_ID")+"";
            String bank_snum = result.get("BANK_SNUM")+"";

            if(bank_snum.length()==3 || bank_snum.length()==7){
                bank_snum = "0"+bank_snum;
                List<Object> params = Lists.newArrayList();
                params.add(bank_snum);
                params.add(bank_id);
                boolean b = jdbcUtils.updateByPreparedStatement(sql_update, params);
                System.out.println("更新完成："+ (b?"TRUE":"FAIL******************"));
            }

        }

        jdbcUtils.releaseConn();
    }

    @Test
    public void findRepeat() throws SQLException {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql_ump = "SELECT * FROM UMP_BANK_CODE";
        String sql_tl = "SELECT * FROM TL_BANK_CODE";

        List<Map<String, Object>> tls = jdbcUtils.findMoreResult(sql_tl, null);
        List<Map<String, Object>> umps = jdbcUtils.findMoreResult(sql_ump, null);

        List<Map<String,Object>> tl_sp = Lists.newArrayList();
        List<Map<String,Object>> ump_sp = Lists.newArrayList();
        List<Map<String,Object>> tl_ump = Lists.newArrayList();

        Set<String> tlBanks = new HashSet<String>();

        for (Map<String,Object> tl : tls){
            String bank_code = tl.get("BANK_CODE")+"";
            String bank_id_tl = tl.get("BANK_ID") + "";
            if(bank_code.length()==3 || bank_code.length()==7) bank_code="0"+bank_code;
            if("0313".equals(bank_code) || "0314".equals(bank_code) || "0402".equals(bank_code) || "0401".equals(bank_code)){
                continue;
            }
            boolean flag = false ;
            for (Map<String,Object> ump : umps){
                String bank_snum = ump.get("BANK_SNUM")+"";
                if(bank_code.equals(bank_snum)) {
                    tl_ump.add(ump);
                    flag = true ;
                }
            }
            if(!flag) tlBanks.add(bank_id_tl);
        }

        for (Map<String,Object> tu:umps){
            String bank_code_tu = tu.get("BANK_SNUM")+"";
            if("0313".equals(bank_code_tu) || "0314".equals(bank_code_tu) || "0402".equals(bank_code_tu) || "0401".equals(bank_code_tu)){
                continue;
            }
            boolean flag = false ;
            for (Map<String,Object> ump : tl_ump){
                String bank_code_ump = ump.get("BANK_SNUM")+"";
                if(bank_code_tu.equals(bank_code_ump)){
                    flag = true ;
                    break;
                }
            }
            if(!flag){

                ump_sp.add(tu);
            }
        }

        for (String id : tlBanks){
            for (Map<String,Object> tl : tls){
                String bank_id = tl.get("BANK_ID") + "";
                if(id.equals(bank_id)){
                    tl_sp.add(tl);
                }
            }
        }

        System.out.println("打印通联单独支持的银行**************************************************"+tl_sp.size());
        for (Map<String,Object> detail : tl_sp){
            for (Map.Entry<String,Object> entry : detail.entrySet()){
                System.out.print(entry.getKey()+":"+entry.getValue()+" ");
                System.out.print(entry.getValue()+":"+entry.getKey()+"; ");
            }
            System.out.println();
        }
        System.out.println("——————————————————————————————————————————————————————");

        System.out.println("打印联动单独支持的银行**************************************************"+ump_sp.size());
        for (Map<String,Object> detail : ump_sp){
            for (Map.Entry<String,Object> entry : detail.entrySet()){
                System.out.print(entry.getKey()+":"+entry.getValue()+" ");
                System.out.print(entry.getValue()+":"+entry.getKey()+"; ");
            }
            System.out.println();
        }
        System.out.println("——————————————————————————————————————————————————————");

        System.out.println("打印双方都支持的银行**************************************************"+tl_ump.size());
        for (Map<String,Object> detail : tl_ump){
            for (Map.Entry<String,Object> entry : detail.entrySet()){
                System.out.print(entry.getKey()+":"+entry.getValue()+" ");
                System.out.print(entry.getValue()+":"+entry.getKey()+"; ");
            }
            System.out.println();
        }
        System.out.println("——————————————————————————————————————————————————————");



        jdbcUtils.releaseConn();
    }
}
