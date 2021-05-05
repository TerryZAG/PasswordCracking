package com.hrbustsecond.service;



import com.hrbustsecond.entity.Passwordcracking_Maintask;

import java.util.List;

public interface Passwordcracking_MaintaskService {
    //查询任务列表，
    List<Passwordcracking_Maintask> query(Passwordcracking_Maintask mtask);
    //修改任务，
    int update(Passwordcracking_Maintask mtask);
    //增加任务
    int insert(Passwordcracking_Maintask mtask);
}
