package com.hrbustsecond.util;


import java.util.HashMap;
import java.util.Map;


public class Passwordcracking_ThreadMember {
    //保存PC_Thread的成员变量
    private static Map<String ,PC_Thread> thread_map=new HashMap();

    public static void putMap(String key,PC_Thread value){
        thread_map.put(key,value);
    }

    public static void removeMap(String key){
        thread_map.remove(key);
    }

    public static PC_Thread getPC_Thread(String key){
        return thread_map.get(key);
    }
}
