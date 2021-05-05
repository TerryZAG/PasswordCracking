package com.hrbustsecond.dao;



import com.hrbustsecond.entity.Passwordcracking_Maintask;

import java.util.List;

public interface Passwordcracking_MaintaskMapper {
    List<Passwordcracking_Maintask> query(Passwordcracking_Maintask task);
    //增加任务
    int insert(Passwordcracking_Maintask task);
    //修改任务
    int update(Passwordcracking_Maintask task);
}
