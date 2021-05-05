package com.hrbustsecond.service;


import com.hrbustsecond.dao.Passwordcracking_NodeMapper;
import com.hrbustsecond.dao.Passwordcracking_TaskMapper;
import com.hrbustsecond.entity.Passwordcracking_Node;
import com.hrbustsecond.entity.Passwordcracking_Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Passwordcracking_NodeServiceImpl implements Passwordcracking_NodeService {
    @Autowired
    private Passwordcracking_NodeMapper passwordcracking_nodeMapper;
    public void setPasswordcracking_nodeMapper(Passwordcracking_NodeMapper passwordcracking_nodeMapper) {
        this.passwordcracking_nodeMapper = passwordcracking_nodeMapper;
    }

    @Override
    public List<Passwordcracking_Node> query(Passwordcracking_Node node) {
        return passwordcracking_nodeMapper.query(node);
    }

    @Override
    public int update(Passwordcracking_Node node) {
        return passwordcracking_nodeMapper.update(node);
    }

    @Override
    public int insert(Passwordcracking_Node node) {
        return passwordcracking_nodeMapper.insert(node);
    }
}
