package common;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 嘉玮 on 2016-5-9.
 */
public class MapTest {
    @Test
    public void testMap(){
       Map map = new HashMap<String,Object>();
        map.put(null,"aaa");
        Object o = map.get(null);
        System.out.println(o);

    }

    @Test
    public void testInteger(){
        System.out.println(Integer.MIN_VALUE);
    }

}
