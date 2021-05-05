package com.hrbustsecond.service;

import com.hrbustsecond.dao.Passwordcracking_UserMapper;
import com.hrbustsecond.entity.Passwordcracking_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("Passwordcracking_UserService")
public class Passwordcracking_UserServiceImpl implements Passwordcracking_UserService {
    @Autowired
    private Passwordcracking_UserMapper passwordcracking_userMapper;
    public void setPasswordcracking_userMapper(Passwordcracking_UserMapper passwordcracking_userMapper) {
        this.passwordcracking_userMapper = passwordcracking_userMapper;
    }

    @Override
    public List<Passwordcracking_User> query(Passwordcracking_User usr) {
        return passwordcracking_userMapper.query(usr);
    }

    @Override
    public List<Passwordcracking_User> querybyname(Passwordcracking_User usr) {
        return passwordcracking_userMapper.querybyname(usr);
    }

    @Override
    public int update(Passwordcracking_User usr) {
        return passwordcracking_userMapper.update(usr);
    }

    @Override
    public int insert(Passwordcracking_User usr) {
        return passwordcracking_userMapper.insert(usr);
    }
}
