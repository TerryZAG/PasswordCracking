package com.hrbustsecond.service;


import com.hrbustsecond.dao.Passwordcracking_TaskMapper;
import com.hrbustsecond.entity.Passwordcracking_Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Passwordcracking_TaskServiceImpl implements Passwordcracking_TaskService {
    @Autowired
    private Passwordcracking_TaskMapper passwordcracking_taskMapper;
    public void setPasswordcracking_taskMapper(Passwordcracking_TaskMapper passwordcracking_taskMapper) {
        this.passwordcracking_taskMapper = passwordcracking_taskMapper;
    }

    @Override
    public List<Passwordcracking_Task> query(Passwordcracking_Task task) {
        return passwordcracking_taskMapper.query(task);
    }

    @Override
    public List<Passwordcracking_Task> queryAllByUser(Passwordcracking_Task task) {
        return passwordcracking_taskMapper.queryAllByUser(task);
    }

    @Override
    public int update(Passwordcracking_Task task) {
        return passwordcracking_taskMapper.update(task);
    }

    @Override
    public int insert(Passwordcracking_Task task) {
        return passwordcracking_taskMapper.insert(task);
    }
}
