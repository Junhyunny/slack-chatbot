package io.junhyunny.utils;

import io.junhyunny.entity.GeneuinMember;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
public class JsonUtilsTest {

    @Test
    public void test() {
        String json = "{\n"
            + "        \"name\": \"kang3966\",\n"
            + "        \"real_name\": \"강준현\",\n"
            + "        \"profile\": {\n"
            + "            \"real_name\": \"강준현\",\n"
            + "            \"real_name_normalized\": \"강준현\",\n"
            + "            \"display_name\": \"Junhyunny\",\n"
            + "            \"display_name_normalized\": \"Junhyunny\"\n"
            + "        }\n"
            + "    }";
        GeneuinMember geneuinMember = JsonUtils.fromJson(json, GeneuinMember.class);
        log.info(geneuinMember);
    }
}
