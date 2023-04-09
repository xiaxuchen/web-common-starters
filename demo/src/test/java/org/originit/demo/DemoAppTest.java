package org.originit.demo;

import cn.hutool.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.originit.demo.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class DemoAppTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Before
    public void setup() {
        // 实例化方式一
//		mockMvc = MockMvcBuilders.standaloneSetup(new HelloWorldController()).build();
        // 实例化方式二
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testList() throws Exception {
        List<Result> resultList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            final Result result = new Result();
            result.setData(i);
            resultList.add(result);
        }
        final HashMap<String, Object> map = new HashMap<>();
        map.put("results", resultList);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/testList")
                        // 设置返回值类型为utf-8，否则默认为ISO-8859-1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtil.toJsonStr(map)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(JSONUtil.toJsonStr(resultList.get(0))));
    }

    @Test
    public void testBasic() throws Exception {
        final Result result = new Result();
        result.setData(24);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/testBasic")
                        // 设置返回值类型为utf-8，否则默认为ISO-8859-1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtil.toJsonStr(result)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(JSONUtil.toJsonStr(result)));
    }


    @Test
    public void testString() throws Exception {
        final Result result = new Result();
        result.setData("hahah");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/testString")
                        // 设置返回值类型为utf-8，否则默认为ISO-8859-1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtil.toJsonStr(result)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(JSONUtil.toJsonStr(result)));
    }

    @Test
    public void testObj() throws Exception {
        final Result result = new Result();
        result.setData(24);
        final HashMap<String, Object> map = new HashMap<>();
        map.put("result", result);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/testObj")
                        // 设置返回值类型为utf-8，否则默认为ISO-8859-1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtil.toJsonStr(map)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(JSONUtil.toJsonStr(result)));
    }
}