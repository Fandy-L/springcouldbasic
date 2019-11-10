package com.chungke.provider018081;


import com.chungke.provider018081.common.executors.DelegateExecutor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


@Slf4j
public class Test {
    /**

     import java.util.Arrays;
     import java.util.List;
     import java.util.UUID;
     import java.util.concurrent.CompletableFuture;
     import java.util.concurrent.Executor;
     import java.util.concurrent.TimeUnit;
     * 通过循环的方式批量执行Runnable请求
     */
    @org.junit.Test
    public void testForeachTasks() {
        ThreadLocal t = new ThreadLocal();
        long s = System.currentTimeMillis();
        DelegateExecutor executor = new DelegateExecutor(5,100, (int) 0L,5,"test");
        List<CompletableFuture<?>> futures = Stream.of(1,2,3,4,5)
                .map(v->CompletableFuture.runAsync(()->{
                    System.out.println(v.intValue());
                    if(v == 5){
                        throw new RuntimeException("测试错误");
                    }
                },executor)).collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        long elapsed = System.currentTimeMillis() -s;
        log.info("一共耗时:{}", elapsed);
        assertTrue(elapsed < 5200);
        futures.forEach(e->assertTrue(e.isDone()));
        timeSleep();
    }

    /**
     * 通过循环的方式批量执行Callable请求
     */
    @org.junit.Test
    public void testForeachFutures() {
        long s = System.currentTimeMillis();
//        DelegateExecutor executor = new DelegateExecutor();
        DelegateExecutor executor = new DelegateExecutor(5,100, (int) 0L,5,"test");
        List<CompletableFuture<Integer>> futures = Stream.of(1,2,3,4,5)
                .map(v->CompletableFuture.supplyAsync(()->timeSleep(v), executor)).collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        long elapsed = System.currentTimeMillis() - s;
        log.info("一共耗时:{}", elapsed);
        assertTrue(elapsed < 5200);
        futures.forEach(e->
        {
            assertTrue(e.isDone());
            Arrays.asList(1,2,3,4,5).contains(e.getNow(999));
            log.info("return  value:{}",e.getNow(999));
        });
        timeSleep();
    }

    /**
     *  执行完毕后，回调结果
     */
    @org.junit.Test
    public void testBatchRunnable() throws Exception{
        LongAdder i = new LongAdder();
//        DelegateExecutor executor = new DelegateExecutor();
        DelegateExecutor executor = new DelegateExecutor(5,100, (int) 0L,5,"test");
        CompletableFuture.runAsync(this::timeSleep, executor).thenAccept(s-> i.increment());
        CompletableFuture.runAsync(this::timeSleep, executor).thenAccept(s-> i.increment());
        CompletableFuture.runAsync(this::timeSleep, executor).thenAccept(s-> i.increment());
        TimeUnit.SECONDS.sleep(2);
        assertEquals(3, i.sum());
    }

    /**
     * 将多个异步请求组合在一起，然后将所有的值组合成另外一个对象再返回
     */
    @org.junit.Test
    public void testCompositeMultiTasks() {
//        Executor executor = new DelegateExecutor();
        DelegateExecutor executor = new DelegateExecutor(5,100, (int) 0L,5,"test");
        String bsid = UUID.randomUUID().toString();
        MDC.put("bsid", bsid);
        long s = System.currentTimeMillis();
        int  c = CompletableFuture.supplyAsync(()->{
//            new ThreadLocal().set();
            Thread thread = Thread.currentThread();
//            thread.threadLocals;
            CombinedValue v = new CombinedValue();
            v.setA(timeSleep(1));
            log.info("log bsid 0 {} ",MDC.get("bsid"));
            return v;
        }, executor)
                .thenCombine(CompletableFuture.supplyAsync(()->timeSleep(2), executor),(x,y)->{
//                    assertEquals(bsid, MDC.get("bsid"));
                    log.info("log bsid 1 {} ",MDC.get("bsid"));
//                    int j = 1/0;
                    x.setB(y);
                    return x;
                })
                .thenCombine(CompletableFuture.supplyAsync(()->timeSleep(3), executor),(x,y)->{
//                    assertEquals(bsid, MDC.get("bsid"));
//                    System.out.println(MDC.get("bsid"));
                    log.info("log bsid 2 {}",MDC.get("bsid"));
                    x.setC(y);
                    return x;
                })
                .thenApply(CombinedValue::count).join();
        System.out.println(c);
        assertEquals(c, 6);
//        assertTrue((System.currentTimeMillis()-s)<1100);
    }

    /**
     * 将带有请求顺序的调用使用同一个CompletableFuture，最后再把多个聚合组合在一起
     */
    @org.junit.Test
    public void testSequencesMultiTasks() {
        long s = System.currentTimeMillis();
        CompletableFuture<CombinedValue> c = CompletableFuture.supplyAsync(()->{
            CombinedValue v = new CombinedValue();
            v.setA(timeSleep(1));
            return v;
        })
                .thenApply(x->{x.setB(timeSleep(2));return x;})
                .thenApply(x->{x.setC(timeSleep(3));return x;});

        int count = CompletableFuture.supplyAsync(()->{
            CombinedValue v = new CombinedValue();
            v.setE(timeSleep(4));
            return v;
        })
                .thenApply(x->{x.setF(timeSleep(5));return x;})
                .thenApply(x->{
                    CombinedValue v = c.join();
                    x.setA(v.getA());
                    x.setB(v.getB());
                    x.setC(v.getC());
                    return x;
                })
                .thenApply(CombinedValue::count).join();

        assertEquals(count, 15);
//        assertTrue((System.currentTimeMillis()-s)<3100);
    }
    @Data
    private class CombinedValue{
        private int a;
        private int b;
        private int c;
        private int d;
        private int e;
        private int f;

        public int count() {
            return a+b+c+d+e+f;
        }
    }

    public class InternalTask {
        public String v() {
            return "v";
        }
        private boolean flag;

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
        public boolean getFlag() {
            return this.flag;
        }
    }
    private int timeSleep(int i) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }

    private void timeSleep() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
