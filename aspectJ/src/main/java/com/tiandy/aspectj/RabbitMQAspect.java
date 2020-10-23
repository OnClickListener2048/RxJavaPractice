package com.tiandy.aspectj;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class RabbitMQAspect {

    @AfterThrowing(value = "execution(* com.rabbitmq.client.ConnectionFactory.newConnection())",throwing = "exception")
    public void onMQConnectionAfterThrowing(Throwable exception) throws Throwable {
        Log.i("onMQConnectionAfter", "" + exception.getMessage());
    }

    @Before(value = "execution(* com.rabbitmq.client.ConnectionFactory.newConnection())")
    public void onMQConnection(JoinPoint joinPoint) throws Throwable {
        Log.i("onMQConnection", "" + joinPoint.toString());
    }

    @After("execution(* com.rabbitmq.client.ConnectionFactory.setHost(java.lang.String))")
    public void setHost(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            Log.i("setHost", "" + arg);
        }
    }

    @After("execution(* com.rabbitmq.client.ConnectionFactory.setPort(..))")
    public void setPort(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            Log.i("setPort", "" + arg);
        }
    }

    @After("execution(* com.rabbitmq.client.ConnectionFactory.setUsername(java.lang.String))")
    public void setUsername(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            Log.i("setUsername", "" + arg);
        }
    }

    @After("execution(* com.rabbitmq.client.ConnectionFactory.setPassword(java.lang.String))")
    public void setPassword(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            Log.i("setPassword", "" + arg);
        }
    }


    @After("execution(* com.rabbitmq.client.DefaultConsumer.handleDelivery(..))")
    public void handleDelivery(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length==4) {
            if (args[3]!=null) {
                String message = new String((byte[]) args[3]);
                Log.i("message", "" + message);
            }
        }
    }

    @Before("execution(* com.rabbitmq.client.Channel.queueBind(..))")
    public void queueBind(JoinPoint joinPoint) throws Throwable {
        Log.i("queueBind","1111");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            Log.i("queueBind", "" + arg);
        }
    }
}
