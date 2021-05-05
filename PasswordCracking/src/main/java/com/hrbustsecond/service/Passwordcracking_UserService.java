package com.hrbustsecond.service;



import com.hrbustsecond.entity.Passwordcracking_User;

import java.util.List;

public interface Passwordcracking_UserService {
    List<Passwordcracking_User> query(Passwordcracking_User cargo);
    List<Passwordcracking_User> querybyname(Passwordcracking_User cargo);
    int update(Passwordcracking_User cargo);
    int insert(Passwordcracking_User cargo);
}
