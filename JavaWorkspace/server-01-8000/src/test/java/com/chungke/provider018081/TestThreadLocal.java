package com.chungke.provider018081;

import com.chungke.provider018081.common.executors.DelegateExecutor;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestThreadLocal {
    public static ThreadLocal<Integer> num = new ThreadLocal<Integer>();
    public static ThreadLocal<Map<String, Object>> map = new ThreadLocal<>();

    public static void put(int i) {
        num.set(i);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(i + "", "fandy");
        map.set(hashMap);
    }

    public static Integer getNum() {
        return num.get();
    }

    public static Map<String, Object> getMap() {
        return map.get();
    }

    @Test
    public void testThread() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(50);
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
        DelegateExecutor executorService = new DelegateExecutor(5,5,10000,1000,"test");
        int i = 1;
        String bsid = UUID.randomUUID().toString();
        MDC.put("bsid", bsid);
        MyRun myRun = new MyRun(countDownLatch);
        long l = System.currentTimeMillis();
        for(int j = 0;j < 500;j++){
        executorService.execute(myRun);
    }
        countDownLatch.await();
        System.out.println(System.currentTimeMillis() - l);
    }

    class MyRun implements Runnable{
        CountDownLatch countDownLatch;
        int i = 50;

        public MyRun( CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        public void run() {
                put(i);
                System.out.println(getNum());
                System.out.println(getMap());
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                Object o = null;
                if(o.equals(new Object())){

                }
                countDownLatch.countDown();
                i++;
            }

    }
}



