package com.hrbustsecond.service;


import com.hrbustsecond.dao.Passwordcracking_MaintaskMapper;
import com.hrbustsecond.dao.Passwordcracking_TaskMapper;
import com.hrbustsecond.entity.Passwordcracking_Maintask;
import com.hrbustsecond.entity.Passwordcracking_Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Passwordcracking_MaintaskServiceImpl implements Passwordcracking_MaintaskService {
    @Autowired
    private Passwordcracking_MaintaskMapper passwordcracking_maintaskMapper;
    public void setPasswordcracking_maintaskMapper(Passwordcracking_MaintaskMapper passwordcracking_taskMapper) {
        this.passwordcracking_maintaskMapper = passwordcracking_taskMapper;
    }

    @Override
    public List<Passwordcracking_Maintask> query(Passwordcracking_Maintask mtask) {
        return passwordcracking_maintaskMapper.query(mtask);
    }

    @Override
    public int update(Passwordcracking_Maintask mtask) {
        return passwordcracking_maintaskMapper.update(mtask);
    }

    @Override
    public int insert(Passwordcracking_Maintask mtask) {
        return passwordcracking_maintaskMapper.insert(mtask);
    }

}
