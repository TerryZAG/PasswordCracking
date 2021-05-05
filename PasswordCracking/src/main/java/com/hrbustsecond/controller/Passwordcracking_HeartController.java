package com.hrbustsecond.controller;


import com.hrbustsecond.service.Passwordcracking_HeartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping("/pc_hearttask")
public class Passwordcracking_HeartController {

    @Autowired
    private Passwordcracking_HeartService passwordcrackingHeartService;

    //接收心跳包
    @RequestMapping(value = "/breath",method = RequestMethod.GET)
    public void breath(String url){
        passwordcrackingHeartService.putMap(url);
    }
}
