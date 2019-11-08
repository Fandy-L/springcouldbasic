package com.chungke.basic.util;

//package mybatis.dao;
//
//i
//import mybatis.mapper.CustomerMapper;
//import mybatis.pojo.Customer;
//import mybatis.pojo.PageParam;
//
//
//import java.io.*;
//import java.util.List;
//
//public class CustomerQueryDao {
//    public static void main(String[] args) throws IOException {
//        PageParam pageParam = new PageParam();
//        pageParam.setPageNum(2);
//        pageParam.setPageSize(2);
//        new CustomerQueryDao().testQueryCus(pageParam);
//
//    }
//
//    public void testQueryCus(PageParam pageParam) throws FileNotFoundException {
//        Customer customer = new Customer();
//        customer.setClientNam("fandy_lin");
//        customer.setPageNum(2);
//        customer.setPageSize(2);
//
//        InputStream inputStream = new FileInputStream(new File(CustomerQueryDao.class.getResource("/").getPath()+
//                "Mybatis-config.xml"));
////        InputStream resourceAsStream = Resources.getResourceAsStream("Mybatis-config.xml");
//        SqlSessionFactory build =  new SqlSessionFactoryBuilder().build(inputStream);
//        SqlSession sqlSession = build.openSession();
//
////        接口读取mapper
//
//        CustomerMapper cusMap = sqlSession.getMapper(CustomerMapper.class);
//        PageInfo customer1 = new PageInfo<>(cusMap.findCustomer(customer));
//
//
////        xml读取mapper
////        sqlSession.insert("insertCustomer",customer);
//        sqlSession.commit();
//        sqlSession.close();
//    }
//}
