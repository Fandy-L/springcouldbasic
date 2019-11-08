package com.chungke.basic.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapTest {
    private static Map<Integer, String> map=new HashMap<Integer,String>();

    /**  1.HashMap 类映射不保证顺序；某些映射可明确保证其顺序: TreeMap 类
     *   2.在遍历Map过程中,不能用map.put(key,newVal),map.remove(key)来修改和删除元素，
     *   会引发 并发修改异常,可以通过迭代器的remove()：
     *   从迭代器指向的 collection 中移除当前迭代元素
     *   来达到删除访问中的元素的目的。
     *   */
    public static void main(String[] args) {
        map.put(1,"one");
        map.put(2,"two");
        map.put(3,"three");
        map.put(4,"four");
        map.put(5,"five");
        map.put(6,"six");
        map.put(7,"seven");
        map.put(8,"eight");
        map.put(5,"five");
        map.put(9,"nine");
        map.put(10,"ten");
//        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
//        while(it.hasNext()){
//            Map.Entry<Integer, String> entry=it.next();
//            int key=entry.getKey();
//            if(key%2==1){
//                System.out.println("delete this: "+key+" = "+key);
//                //map.put(key, "奇数");   //ConcurrentModificationException
//                //map.remove(key);      //ConcurrentModificationException
//                it.remove();        //OK
//            }
//        }
        map.remove(1);
        //遍历当前的map；这种新的for循环无法修改map内容，因为不通过迭代器。
        System.out.println("-------\n\t最终的map的元素遍历：");
        for(Map.Entry<Integer, String> entry:map.entrySet()){
            int k=entry.getKey();
            String v=entry.getValue();
            System.out.println(k+" = "+v);
        }
    }
}