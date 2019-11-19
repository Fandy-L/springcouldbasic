package com.to8to.tbt.msc.controller;

import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageCenterControllerTest extends BaseApplication {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void searchMessageRecord() throws Exception {
        String url = "/search/message/record";

        List<SearchMessageRecordDTO> searchMessageRecordDTOList = new ArrayList<>();
        searchMessageRecordDTOList.add(SearchMessageRecordDTO.builder()
                .etime(10)
                .build());
        searchMessageRecordDTOList.add(SearchMessageRecordDTO.builder()
                .etime(11)
                .build());

        String content = JSONObject.toJSONString(searchMessageRecordDTOList);

        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void listMsg() throws Exception {
        String url = "/list/msg";

        Map<String,Object> pageInfo = new HashMap<>();
        pageInfo.put("curr_page",2);
        pageInfo.put("page_size", 5);

        Map<String, Long> searchTime = new HashedMap();
        searchTime.put("start_time", (long) 156515881);
        searchTime.put("end_time", (long) 156515890);

        List<String> contactList = new ArrayList<>();
        contactList.add("3");
        contactList.add("1");

        Map<String,Object> map = new HashMap<>();
        map.put("ns", "pc");
        map.put("pageInfo", pageInfo);
        //map.put("searchTime",searchTime );
        map.put("send_type", 5);
        map.put("target_contact", "573608");
        map.put("to_user_type", 2);
        map.put("send_status",2);
        map.put("content", "恭喜，您公司的资质证书审核通过！");
        map.put("title", "资质证书审核通过");
        map.put("note_id", 902);
        //map.put("contactlist",contactList);
        //map.put("yjindu", 5);
        //map.put("tid",1001);
        //map.put("yid", 10000);
       // map.put("is_read", 1);
       // map.put("user_id", 1551);
        String content = JSONObject.toJSONString(map);

        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void setAppMsgReaded() throws Exception {
        String url ="/set/app/msg/readed";

        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("1"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void setAppMsgReadedBatch() {
    }

    @Test
    public void setAppMsgStatusByUidAndNodeId() throws Exception {
        String url = "/set/app/msg/status/node";

        List<Integer> nodeList = new ArrayList<>();
        nodeList.add(154);
        nodeList.add(150);

        Map<String,Object> map = new HashMap<>();
        map.put("uid", 10);
        map.put("is_read",0);
        map.put("node_ids",nodeList);
        String content = JSONObject.toJSONString(map);

        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}