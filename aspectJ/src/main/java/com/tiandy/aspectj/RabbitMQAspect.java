package com.tiandy.aspectj;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class RabbitMQAspect {

    @AfterThrowing("execution(* com.rabbitmq.client.ConnectionFactory.newConnection())")
    public void onMQConnection(Exception exception) throws Throwable {
        Log.i("helloAOP", "" + exception.getMessage());
    }

    @After("execution(* com.rabbitmq.client.ConnectionFactory.setHost(java.lang.String))")
    public void setHost(JoinPoint joinPoint) throws Throwable {
        Log.i("helloAOP", "" + joinPoint.getArgs()[0]);
    }

    @After("execution(* com.rabbitmq.client.ConnectionFactory.setPort(..))")
    public void setPort(JoinPoint joinPoint) throws Throwable {
        Log.i("helloAOP", "" + joinPoint.getArgs()[0]);
    }

    @After("execution(* com.rabbitmq.client.ConnectionFactory.setUsername(java.lang.String))")
    public void setUsername(JoinPoint joinPoint) throws Throwable {
        Log.i("helloAOP", "" + joinPoint.getArgs()[0]);
    }

    @After("execution(* com.rabbitmq.client.ConnectionFactory.setPassword(java.lang.String))")
    public void setPassword(JoinPoint joinPoint) throws Throwable {
        Log.i("helloAOP", "" + joinPoint.getArgs()[0]);
    }
}
