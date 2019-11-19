package com.to8to.tbt.msc.controller;

import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.BaseApplication;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AppMsgControllerTest extends BaseApplication {



    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    }

    @Test
    public void followList() throws Exception {
        String url = "/app/follow/list";

        Map<String,Object> map = new HashMap<>();

        //输入小数直接取整数位,先取整再判断范围,可以输入字符串，但字符串内容只能是整数。
        map.put("uid",1515);//大于1
        map.put("page","4");//page值最小为1
        map.put("pageSize","5");//1-100
        String string = JSONObject.toJSONString(map);

        mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(string))
                .andExpect(status().isOk());
    }

    @Test
    public void interactionList() throws Exception {
        String url = "/app/interactive/list";

        Map<String,Object> map = new HashMap<>();
        map.put("uid", 6881);
        map.put("page", 5);
        map.put("pageSize", 101);
        String string = JSONObject.toJSONString(map);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(string))
                .andExpect(status().isOk());

    }

    @Test
    public void processMsgList() throws Exception {
        String url = "/app/process/list";

        Map<String,Object> map = new HashMap<>();
        map.put("uid", 1551);
        map.put("page", 5);
        map.put("pageSize", 10);
        String string = JSONObject.toJSONString(map);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(string))
                .andExpect(status().isOk());

    }

    @Test
    public void msgSetHasRead() throws Exception{
        String url = "/app/read/clear";

        Map<String,Object> map = new HashMap<>();

        String content = JSONObject.toJSONString(map);
        map.put("appid", -100);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(content))
                .andExpect(status().isOk());
    }
}