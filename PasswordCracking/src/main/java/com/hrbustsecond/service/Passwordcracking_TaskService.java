package com.hrbustsecond.service;



import com.hrbustsecond.entity.Passwordcracking_Task;

import java.util.List;

public interface Passwordcracking_TaskService {
    //查询任务列表，
    List<Passwordcracking_Task> query(Passwordcracking_Task task);
    //发布人/参与人
    List<Passwordcracking_Task> queryAllByUser(Passwordcracking_Task task);
    //修改任务，状态，password
    int update(Passwordcracking_Task task);
    //增加任务
    int insert(Passwordcracking_Task task);
}
