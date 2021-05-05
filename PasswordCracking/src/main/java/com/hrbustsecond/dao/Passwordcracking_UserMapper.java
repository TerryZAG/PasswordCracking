package com.hrbustsecond.dao;


import com.hrbustsecond.entity.Passwordcracking_User;

import java.util.List;

public interface Passwordcracking_UserMapper {
    //登陆查询账号是否存在,查询在线用户
    List<Passwordcracking_User> query(Passwordcracking_User user);
    List<Passwordcracking_User> querybyname(Passwordcracking_User user);
    //新增（首次登陆）
    int insert(Passwordcracking_User user);
    //修改（更改在线状态）
    int  update(Passwordcracking_User user);
}
