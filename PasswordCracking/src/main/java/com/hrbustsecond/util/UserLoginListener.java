package com.hrbustsecond.util;


import com.hrbustsecond.entity.Passwordcracking_User;
import com.hrbustsecond.service.Passwordcracking_UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class UserLoginListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println("创建session......");
        ServletContext context=httpSessionEvent.getSession().getServletContext();
        Integer count=(Integer)context.getAttribute("count");
        if(count==null){
            count=new Integer(1);
        }else{
            int co = count.intValue( );
            count= new Integer(co+1);
        }
        System.out.println("当前用户人数："+count);
        context.setAttribute("count", count);//保存人数
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        System.out.println("销毁session......");
        ServletContext context = httpSessionEvent.getSession().getServletContext();
        Integer count = (Integer) context.getAttribute("count");
        int co = count.intValue();
        count = new Integer(co - 1);
        context.setAttribute("count", count);
        //获取登录user
        Passwordcracking_User usr = (Passwordcracking_User) httpSessionEvent.getSession().getAttribute("loginuser");
        if(null!=usr){
            usr.setOnlinestate(0);
            //获取service
            Passwordcracking_UserServiceImpl passwordcrackingUserService = (Passwordcracking_UserServiceImpl)this.getObjectFromApplication(context,"Passwordcracking_UserService");
            passwordcrackingUserService.update(usr);
        }
        System.out.println("当前用户人数：" + count);
    }

    private Object getObjectFromApplication(ServletContext servletContext,String beanName){
        //通过WebApplicationContextUtils 得到Spring容器的实例。
        ApplicationContext application=WebApplicationContextUtils.getWebApplicationContext(servletContext);
        //返回Bean的实例。
        return application.getBean(beanName);
    }
}
