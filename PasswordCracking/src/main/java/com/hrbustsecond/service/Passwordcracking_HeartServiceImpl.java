package com.hrbustsecond.service;


import com.hrbustsecond.dao.Passwordcracking_NodeMapper;
import com.hrbustsecond.entity.Passwordcracking_Node;
import com.hrbustsecond.util.HttpRequest;
import com.hrbustsecond.util.PC_Thread;
import com.hrbustsecond.util.Passwordcracking_ThreadMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class Passwordcracking_HeartServiceImpl implements Passwordcracking_HeartService {
    @Autowired
    private Passwordcracking_NodeMapper passwordcracking_nodeMapper;
    public void setPasswordcracking_nodeMapper(Passwordcracking_NodeMapper passwordcracking_nodeMapper) {
        this.passwordcracking_nodeMapper = passwordcracking_nodeMapper;
    }

    @Override
    public HashMap<String, Long> getMap() {
        return Passwordcracking_HeartService.map;
    }

    @Override
    public void putMap(String name) {
        Passwordcracking_HeartService.map.put(name,getTime());
    }

    @Override
    public void checkMap() {
        //遍历map，检查全部在线节点
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String oldNodeName =(String) entry.getKey();//线程名，节点名
            //阻塞的线程runnable实例
            PC_Thread oldPcThread = Passwordcracking_ThreadMember.getPC_Thread(oldNodeName);
            if(isTimeout(oldNodeName)){//如果超时了
                //从心跳列表中删除节点
                deleteMap(oldNodeName);
                //查询新节点
                Passwordcracking_Node querynode = new Passwordcracking_Node();
                querynode.setPC_State(1);
                Passwordcracking_Node node = passwordcracking_nodeMapper.query(querynode).get(0);
                //如果还有新节点
                if(null!=node){
                    //分配新节点
                    StringBuilder newNodeName = HttpRequest.getIPandPort(node.getPC_Host(),Integer.parseInt(node.getPC_Port()));
                    //新线程runnable实例
                    PC_Thread newPcThread = new PC_Thread(node,oldPcThread.getMap(),oldPcThread.getTask());
                    //新线程
                    Thread thread=new Thread(newPcThread);
                    //添加到runnable实例列表
                    Passwordcracking_ThreadMember.putMap(newNodeName.toString(),newPcThread);
                    thread.setName(newNodeName.toString());
                    thread.start();
                }
            }
            //停止原线程，删除runnable实例
            oldPcThread.stopThread();
            Passwordcracking_ThreadMember.removeMap(oldNodeName);
            //System.out.println(isTimeout(key));
        }
    }

    @Override
    public void deleteMap(String name) {
        Passwordcracking_HeartService.map.remove(name);
    }

    public boolean isTimeout(String name) {
        //获取心跳时间戳
        Long heartTime = Passwordcracking_HeartService.map.get(name);
        //当前时间
        Long nowTime = getTime();
        //如果（当前时间戳-心跳时间戳）>设置的阈值，就断开连接
        if((nowTime - heartTime)>300000){
            return true;
        }else{
            return false;
        }
    }

    private Long getTime(){
        //获取当前时间戳
        return (new Date()).getTime();
    }

    private Thread[] getThreads(){
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = currentGroup;
        // 遍历线程组树，获取根线程组
        while (currentGroup != null) {
            topGroup = currentGroup;
            currentGroup = currentGroup.getParent();
        }
        int nowThreads = topGroup.activeCount();
        Thread[] lstThreads = new Thread[nowThreads*2];
        topGroup.enumerate(lstThreads);
        return lstThreads;
    }
}
