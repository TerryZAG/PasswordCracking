package com.hrbustsecond.util;

import com.hrbustsecond.service.Passwordcracking_HeartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public  class HeartBeatTask {
    @Autowired
    private Passwordcracking_HeartService passwordcrackingHeartService;

    //@Scheduled(cron = "1/5 * * * * ?")
    //@Scheduled(cron = "0 1/5 * * * ? ")
    @Scheduled(cron = "0/1 * * * * ?")
    public void HBTask(){
        passwordcrackingHeartService.checkMap();
        //System.out.println(System.currentTimeMillis());
    }
}
