package com.hrbustsecond.controller;

import com.hrbustsecond.entity.Passwordcracking_User;
import com.hrbustsecond.service.Passwordcracking_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/pc_usercontroller")
public class Passwordcracking_UserController {

    @Autowired
    private Passwordcracking_UserService passwordcrackingUserService;
    //登陆
    @RequestMapping(value = "/login",method = RequestMethod.POST)//登陆post方法
    public String query(Passwordcracking_User loginuser, Model model,HttpSession session){
        //查询账户是否存在
        List<Passwordcracking_User> usrList = passwordcrackingUserService.querybyname(loginuser);
        if(usrList.size()>0){//存在
            //更改在线状态为上线
            Passwordcracking_User usr = usrList.get(0);
            usr.setOnlinestate(1);
            passwordcrackingUserService.update(usr);
            //session域
            session.setAttribute("loginuser",usr);
        }else {
            loginuser.setOnlinestate(1);
            int id = passwordcrackingUserService.insert(loginuser);
            loginuser.setId(id);
            //session域
            session.setAttribute("loginuser",loginuser);
        }
        //System.out.println(loginuser.getName());
        return "crackingview";
    }
    //退出
    @RequestMapping(value = "/close")//销毁session
    public void close(HttpSession session) {
        //无效化session
        session.invalidate();
    }
    //定时请求
    @RequestMapping(value = "/transmit")//销毁session
    @ResponseBody
    public String transmit(HttpSession session) {
        return "1";
    }
}
