package com.hrbustsecond.controller;

import com.hrbustsecond.entity.*;
import com.hrbustsecond.service.Passwordcracking_MaintaskService;
import com.hrbustsecond.service.Passwordcracking_NodeService;
import com.hrbustsecond.service.Passwordcracking_TaskService;
import com.hrbustsecond.service.Passwordcracking_UserService;
import com.hrbustsecond.util.HttpRequest;
import com.hrbustsecond.util.PC_Thread;
import com.hrbustsecond.util.Passwordcracking_ThreadMember;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/pc_dircontroller")
public class Passwordcracking_TaskController {
    @Autowired
    private Passwordcracking_UserService passwordcrackingUserService;
    @Autowired
    private Passwordcracking_TaskService passwordcrackingTaskService;
    @Autowired
    private Passwordcracking_MaintaskService passwordcrackingMaintaskService;
    @Autowired
    private Passwordcracking_NodeService passwordcrackingNodeService;

    //提交任务
    @RequestMapping(value = "/pc_save",method = RequestMethod.POST)//登陆post方法
    @ResponseBody
    public String makedir(Passwordcracking_Dir dir, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取字典字符空间
        List charspace = dir.getDirspace();
        //获取MD5
        String PC_MD5 = dir.getPC_MD5();
        //先查询后台是否有结果
        Passwordcracking_Task queryTask = new Passwordcracking_Task();
        queryTask.setPC_MD5(PC_MD5);
        queryTask.setPC_State(2);
        List<Passwordcracking_Task> queryTasks= passwordcrackingTaskService.query(queryTask);
        if(queryTasks.size()>0){
            response.setCharacterEncoding("UTF-8");
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(queryTasks);
        }else{
            //获取在线人数
            ServletContext context = session.getServletContext();
            Integer count = (Integer) context.getAttribute("count");
            //-----------------------------------------------------------------------------------
            //选择节点数
            count = dir.getPC_Nodenum();
            //-----------------------------------------------------------------------------------
            //获取在线用户列表
            //Passwordcracking_User usr = new Passwordcracking_User();
            //usr.setOnlinestate(1);
            //List<Passwordcracking_User> onlineUsers = passwordcrackingUserService.query(usr);
            //获取发布任务的用户
            Passwordcracking_User loginusr =(Passwordcracking_User) session.getAttribute("loginuser");
            //总任务数
            double tasknum = 0;//总任务数
            int s =Integer.parseInt(charspace.get(charspace.size()-2).toString());//原文长度开始
            int e= Integer.parseInt(charspace.get(charspace.size()-1).toString());//原文长度结束
            charspace.remove(charspace.size()-1);
            charspace.remove(charspace.size()-1);
            int dirlength = charspace.size();//字符空间长度
        //        for( int i=s ;i<=e;i++){
        //            tasknum = tasknum+Math.pow(Double.valueOf(dirlength),Double.valueOf(i));
        //        }
            //生成字典
            List<String> wrongDirresult = permute(charspace,s,e);
            //去重
            List<String> dirResult = new ArrayList<>();//dirResult是字典
            HashSet hs = new HashSet();
            for (String item:wrongDirresult) {
                if(hs.add(item)){
                    dirResult.add(item);
                }
            }
            //平均
            tasknum = dirResult.size();
            int avg =(int) Math.floor(tasknum/count);
            //System.out.println(dirResult.toString());
            //分割任务,生成文件
            String usrName = loginusr.getName();

            int PC_Maintaskid = 0;//主任务的id，insert后复制
            //System.out.println(onlineUsers.toString());
//            for(int p = 0;p<count;p++){//排序用户，把发布任务用户放在第一个
//                if(onlineUsers.get(p).getId()==loginusr.getId()){
//                    usr = onlineUsers.get(0);
//                    onlineUsers.set(0,onlineUsers.get(p));
//                    onlineUsers.set(p,usr);
//                }
//            }
            //System.out.println(onlineUsers.toString());
            int dirPoint = 0;
            int i = 0;
            for(int p = 0;p<count;p++){
                //将日期作为文件名
                Date date = new Date();
                String dateStr = String.valueOf(date.getTime());
                String fileName = usrName+dateStr;
                String contexPath= request.getSession().getServletContext().getRealPath("/");//绝对路径
                String fileFullPath = contexPath+"dirfiles"+File.separator+fileName;
                File file = new File(fileFullPath);
        //            File f = new File(filePath);
        //            if (!f.exists()) {
        //                System.out.println(filePath);
        //            }
                //System.out.println(filePath);
                file.createNewFile();
                //List<String> dirS = new ArrayList<>();
                BufferedWriter out=new BufferedWriter(new FileWriter(file));
                try
                {
                    out.write("[");
                    while(i<avg||(p==(count-1)&&dirPoint<dirResult.size())){//bug-----------------------
        //                    out.write(dirResult.get(dirPoint));
        //                    out.newLine();
                        //dirS.add(dirResult.get(dirPoint));
                        if("'".equals(dirResult.get(dirPoint))){
                            out.write("'\\'',");
                        }else if("".equals(dirResult.get(dirPoint))){
                            out.write("',',");
                        }else{
                            out.write("'"+dirResult.get(dirPoint)+"',");
                        }
                        i++;
                        dirPoint++;
                    }
                } catch (IOException ie)
                {
                    // TODO Auto-generated catch block
                    ie.printStackTrace();
                }
                out.write("]");
                out.close();
                //向数据库插入一条任务
                Passwordcracking_Task pwTask = new Passwordcracking_Task();
                pwTask.setPC_File(fileFullPath);
                pwTask.setPC_MD5(PC_MD5);
                pwTask.setPC_State(1);
                //passwordcrackingTask.setPC_Password();
                //if(onlineUsers.get(p).getId()==loginusr.getId()){
                if(p==0){//插入主任务
                    pwTask.setPC_Userid(loginusr.getId());
                    passwordcrackingTaskService.insert(pwTask);
                    PC_Maintaskid = pwTask.getId();
                }else{//插入附加任务
                    //pwTask.setPC_Partuserid(onlineUsers.get(p).getId());
                    pwTask.setPC_Maintaskid(PC_Maintaskid);
                    passwordcrackingTaskService.insert(pwTask);
                }
            }
            response.setCharacterEncoding("UTF-8");
            String tasks=this.getmission(session,request,response);//bug-----------------------
            return tasks;
        }
    }

    //字典生成
    private List<String> permute(List<Object> charspace,int s,int e){
        List<String> list = new ArrayList<>();//结果
        int fullCharLength = charspace.size();
        for( int i=s ;i<=e;i++) {//i=原文长度
            int counter = 0;
            StringBuilder buider = new StringBuilder();
            while (buider.toString().length() <= i) {
                buider = new StringBuilder(i*2);
                int _counter = counter;
                //10进制转换成26进制
                while (_counter >= fullCharLength) {
                    //获得低位
                    buider.insert(0, charspace.get(_counter % fullCharLength));
                    _counter = _counter / fullCharLength;
                    //精髓所在，处理进制体系中只有10没有01的问题，在穷举里面是可以存在01的
                    _counter--;
                }
                //最高位
                buider.insert(0,charspace.get(_counter));
                counter++;
                list.add(buider.toString());
            }
        }
        return list;
    }

    //查询字典
    @RequestMapping(value = "/pc_getdirspace",method = RequestMethod.POST)
    @ResponseBody
    private String getdirspace(Passwordcracking_Task task, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuffer dir = getfile(task.getPC_File());
        task.setPC_Filedir(dir.toString());
        ObjectMapper om = new ObjectMapper();//json转换
        return om.writeValueAsString(task);
    }

    //请求任务
    @RequestMapping(value = "/pc_getmission",method = RequestMethod.POST)
    @ResponseBody//返回值由视图解析变为json数据
    public String pc_getmission(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException{
        String tasks = this.getmission(session,request,response);
        //System.out.println(tasks);
        return tasks;
    }

    //检索任务
    private String getmission( HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Passwordcracking_Task task = new Passwordcracking_Task();
        int userid = ((Passwordcracking_User) session.getAttribute("loginuser")).getId();
        task.setPC_Userid(userid);
        task.setPC_Partuserid(userid);
        List<Passwordcracking_Task> tasks = passwordcrackingTaskService.queryAllByUser(task);
//        for(int i=0;i<tasks.size();i++){
//            if(tasks.get(i).getPC_State()==1){
//                String dir = getfile(tasks.get(i).getPC_File());
//                tasks.get(i).setPC_Filedir(dir);
//            }
//        }
        ObjectMapper om = new ObjectMapper();//json转换
        return om.writeValueAsString(tasks);
    }

    //io流读取文件
    private StringBuffer getfile(String path){
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            while (in.ready()) {
                sb = (new StringBuffer(in.readLine()));
                //System.out.println(sb);
            }
            in.close();
        } catch (IOException e) {
        }
        return sb;
    }

//    @RequestMapping(value = "/pc_cracking",method = RequestMethod.POST)
//    @ResponseBody
//    private String pc_cracking( Passwordcracking_Task task, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response){
//        //file
//        String file = task.getPC_File();
//        StringBuffer sb = new StringBuffer();
//        try {
//            BufferedReader in = new BufferedReader(new FileReader(file));
//            while (in.ready()) {
//                sb = (new StringBuffer(in.readLine()));
//                //System.out.println(sb);
//            }
//            in.close();
//        } catch (IOException e) {
//        }
//
//        return sb.toString();
//    }

    //破解成功
    @RequestMapping(value = "/pc_crackingsuc",method = RequestMethod.POST)
    @ResponseBody
    private String crackingsuc( Passwordcracking_Task task, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        //查询条件
        Passwordcracking_Task queryTask = new Passwordcracking_Task();
        //次要任务
        List<Passwordcracking_Task> tasks ;
        //主要任务
        Passwordcracking_Task maintask ;
        int mainTaskid = task.getPC_Maintaskid();
        if(mainTaskid>0){//次要任务记录
            queryTask.setPC_Maintaskid(task.getPC_Maintaskid());
            tasks = passwordcrackingTaskService.query(queryTask);
            queryTask.setId(task.getPC_Maintaskid());
            queryTask.setPC_Maintaskid(0);
            maintask = (passwordcrackingTaskService.query(queryTask)).get(0);
        }else{//主要任务记录
            queryTask.setPC_Maintaskid(task.getId());
            tasks = passwordcrackingTaskService.query(queryTask);
            queryTask.setId(task.getId());
            queryTask.setPC_Maintaskid(0);
            maintask = (passwordcrackingTaskService.query(queryTask)).get(0);
        }
        for(int i=0;i<tasks.size();i++){
            tasks.get(i).setPC_Process(100);
            tasks.get(i).setPC_State(2);
            tasks.get(i).setPC_Password(task.getPC_Password());
            passwordcrackingTaskService.update(tasks.get(i));
        }
        maintask.setPC_Process(100);
        maintask.setPC_State(2);
        maintask.setPC_Password(task.getPC_Password());
        passwordcrackingTaskService.update(maintask);
        return "1";
    }
    //破解失败
    @RequestMapping(value = "/pc_crackingfai",method = RequestMethod.POST)
    @ResponseBody
    private String crackingfai( Passwordcracking_Task task, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        //查询条件
        Passwordcracking_Task queryTask = new Passwordcracking_Task();
        //次要任务
        List<Passwordcracking_Task> tasks ;
        //主要任务
        Passwordcracking_Task maintask ;
        if(task.getPC_Maintaskid()>0){//次要记录记录
            queryTask.setPC_Maintaskid(task.getPC_Maintaskid());
            tasks = passwordcrackingTaskService.query(queryTask);
            queryTask.setId(task.getPC_Maintaskid());
            queryTask.setPC_Maintaskid(0);
            maintask = (passwordcrackingTaskService.query(queryTask)).get(0);
        }else{//主任务记录
            queryTask.setPC_Maintaskid(task.getId());
            tasks = passwordcrackingTaskService.query(queryTask);
            queryTask.setId(task.getId());
            queryTask.setPC_Maintaskid(0);
            maintask = (passwordcrackingTaskService.query(queryTask)).get(0);
        }
        int pro = 100/(tasks.size()+1);
        int sum = maintask.getPC_Process()+pro;
        if(maintask.getId()==task.getId()){
            maintask.setPC_State(3);
        }else{
            for(int i=0;i<tasks.size();i++){
                if(tasks.get(i).getId()==task.getId()){
                    tasks.get(i).setPC_State(3);
                    break;
                }
            }
        }
        if(sum>100){
            for(int i=0;i<tasks.size();i++){
                tasks.get(i).setPC_Process(100);
                passwordcrackingTaskService.update(tasks.get(i));
            }
            maintask.setPC_Process(100);
        }else{
            for(int i=0;i<tasks.size();i++){
                tasks.get(i).setPC_Process(sum);
                passwordcrackingTaskService.update(tasks.get(i));
            }
            maintask.setPC_Process(sum);
        }
        passwordcrackingTaskService.update(maintask);
        return "1";
    }

    //node.js破解
    @RequestMapping(value = "/pc_crackingnodejs",method = RequestMethod.POST)
    @ResponseBody
    private String crackingnodejs( Passwordcracking_Task task, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        String res = null;
        //字段
        int id = task.getId();
        String PC_MD5 = task.getPC_MD5();
        //查询相关全部任务
        Passwordcracking_Task queryTask = new Passwordcracking_Task();//查询条件
        queryTask.setPC_Maintaskid(id);//where PC_Maintaskid = id
        //查询结果
        List<Passwordcracking_Task> tasks = passwordcrackingTaskService.query(queryTask);
        //所有的任务
        tasks.add(task);
        //获取空闲节点
        Passwordcracking_Node querynode = new Passwordcracking_Node();
        querynode.setPC_State(1);
        List<Passwordcracking_Node> nodes = passwordcrackingNodeService.query(querynode);
        int num_nodes = nodes.size();//可用节点数量
        if(tasks.size()>num_nodes){
            return "0";//可用节点数量不够
        }
        //分发任务
        for(int i=0;i<tasks.size();i++){
            //任务
            Passwordcracking_Task targetTask = tasks.get(i);
            //节点
            Passwordcracking_Node node = nodes.get(i);
            //字典
            StringBuffer dir = getfile(targetTask.getPC_File());
            Map map=new HashMap();
            ObjectMapper om = new ObjectMapper();//json转换
            try {
                map.put("dir",om.writeValueAsString(dir.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put("pc_md5",PC_MD5);
            //设置节点被占用
            node.setPC_State(2);
            passwordcrackingNodeService.update(node);
            //节点别名
            StringBuilder iPandPort = HttpRequest.getIPandPort(node.getPC_Host(),Integer.parseInt(node.getPC_Port()));
            //runnable实例
            PC_Thread pc_thread = new PC_Thread(node,map,task);
            //保存runnable实例
            Passwordcracking_ThreadMember.putMap(iPandPort.toString(),pc_thread);
            //创建新线程
            Thread thread=new Thread(pc_thread);
            thread.setName(iPandPort.toString());
            thread.start();
        }
        //成功开始破解后
        return "1";
    }
}