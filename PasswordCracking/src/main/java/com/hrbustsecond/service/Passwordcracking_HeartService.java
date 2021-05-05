package com.hrbustsecond.service;


import java.util.HashMap;

public interface Passwordcracking_HeartService {
    public static final HashMap<String ,Long> map = new HashMap();
    public abstract HashMap<String ,Long> getMap();
    public abstract void putMap(String name);
    public abstract void checkMap();
    public abstract void deleteMap(String name);
}
