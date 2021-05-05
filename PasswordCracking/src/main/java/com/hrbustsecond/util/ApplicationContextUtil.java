package com.hrbustsecond.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContextUtil  implements ApplicationContextAware {

    private static ApplicationContext context;
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
    public static ApplicationContext getContext() {
        return context;
    }
    public static Object getBean(String name){
        return context.getBean(name);
    }
    public static <T> T getBean(Class<T> clazz) {
        return getContext().getBean(clazz);
    }
}
