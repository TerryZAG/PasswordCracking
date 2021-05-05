package com.hrbustsecond.util;

import com.hrbustsecond.entity.Passwordcracking_Node;
import com.hrbustsecond.entity.Passwordcracking_Task;
import com.hrbustsecond.service.Passwordcracking_HeartService;
import com.hrbustsecond.service.Passwordcracking_NodeService;
import com.hrbustsecond.service.Passwordcracking_TaskService;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

public class PC_Thread implements Runnable{

    Passwordcracking_Node node;
    Map map;
    Passwordcracking_Task task;
    public PC_Thread(Passwordcracking_Node node,Map map,Passwordcracking_Task task){
        this.node = node;
        this.map = map;
        this.task = task;
    }

    Passwordcracking_TaskService passwordcrackingTaskService = (Passwordcracking_TaskService)ApplicationContextUtil.getBean(Passwordcracking_TaskService.class);
    Passwordcracking_NodeService passwordcrackingNodeService = (Passwordcracking_NodeService)ApplicationContextUtil.getBean(Passwordcracking_NodeService.class);
    Passwordcracking_HeartService passwordcrackingHeartService = (Passwordcracking_HeartService)ApplicationContextUtil.getBean(Passwordcracking_HeartService.class);

    private boolean stopThread = true;

    public void stopThread() {
        stopThread = false;
    }
    public Map  getMap() {
        return map;
    }
    public Passwordcracking_Task getTask() {
        return task;
    }

    @Override
    public void run() {
        //添加到心跳检测列表
        StringBuilder iPandPort = HttpRequest.getIPandPort(node.getPC_Host(),Integer.parseInt(node.getPC_Port()));
        passwordcrackingHeartService.putMap(iPandPort.toString());
        while (stopThread){
            //        try {
//            Thread.sleep(30000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
            //System.out.println(Thread.currentThread().getName());
            //开始破解
            String res = null;
            try {
                //通过node.js破解
                res = HttpRequest.callHttpPost( iPandPort,node.getPC_Host(),Integer.parseInt(node.getPC_Port()),"passwordcrackingnodejs",map);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (res != null) {
                String bool = res.substring(0,1);
                if("1".equals(bool)){
                    String password = res.substring(1,res.length());
                    //System.out.println(password);
                    //保存结果
                    task.setPC_Password(password);
                    task.setPC_Process(100);
                    task.setPC_State(2);
                    passwordcrackingTaskService.update(task);
                }
            }
            //改变节点状态为空闲
            node.setPC_State(1);
            passwordcrackingNodeService.update(node);
        }
        //从心跳检测列表删除
        passwordcrackingHeartService.deleteMap(iPandPort.toString());
        //删除runnable实例
        Passwordcracking_ThreadMember.removeMap(iPandPort.toString());
    }
}
