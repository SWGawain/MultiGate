package jackson;

import com.google.common.collect.Sets;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

/**
 * Created by 嘉玮 on 2016-4-1.
 */
public class TestJackson {
    @Test
    public void testSet() throws IOException {
        Set<String> keys = Sets.newHashSet();
        keys.add("aaa");
        keys.add("bbb");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(keys);
        System.out.println(json);
    }
}
