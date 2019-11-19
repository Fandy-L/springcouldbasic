//package com.to8to.tbt.msc.service;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvcBuilder;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.MultiValueMap;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class QuickAppTest {
//    private MockMvc mockMvc;
//
//    @Before
//    public void serUp(){
//        mockMvc = MockMvcBuilders.standaloneSetup(new QuickAppTest()).build();
//    }
//
//    /**
//     * 新增单个用户信息 /users/ POST
//     * 注意 addUser 使用了 @RequestBody 方法，对应使用 .contentType(MediaType.APPLICATION_JSON).content(json 字符串)
//     * */
//    @Test
//    public void addUser() throws  Exception{
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/saveDeviceInfo")
//                .
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//        String contentAsString = mvcResult.getResponse().getContentAsString();
//        System.out.println(contentAsString);
//    }
//
//
//}
