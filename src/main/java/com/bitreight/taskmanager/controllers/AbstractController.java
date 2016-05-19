package com.bitreight.taskmanager.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractController {

    protected AbstractController() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("/spring/spring-context.xml");
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }
}
