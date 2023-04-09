package com.originit.common;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.junit.Test;

public class JSONTest {
    @Test
    public void test1() {
        final JSON parse = JSONUtil.parse("{\"name\":\"xxc\",\"age\":24,\"wifi\":{\"name\":\"zss\",\"age\":25}}");
        System.out.println(parse);
    }
}
