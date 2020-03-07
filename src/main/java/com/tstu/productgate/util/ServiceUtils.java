package com.tstu.productgate.util;

import com.tstu.productgate.feign.autentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ServiceUtils {
    private static ServiceUtils instance;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void fillInstance() {
        instance = this;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public ServiceUtils(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static AuthenticationService getAuthenticationService() {
        return instance.applicationContext.getBean(AuthenticationService.class);
    }

}
