package com.example.rxjavapractice;

import com.blankj.utilcode.util.LogUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RabbitMQThread {
    private ConnectionFactory factory;
    private Connection connection;
    private String ip;
    private Channel alarmChannel;

    public RabbitMQThread(String ip) {
        this.ip = ip;
    }


    //接收下载人员数据消息
    //接收上传当前人员数据消息
    //远程开门


    public void init() {
        setupConnectionFactory();
        buildMQConnection();
    }


    private void setupConnectionFactory() {
        factory = new ConnectionFactory();
        factory.setHost(ip);
        factory.setPort(5673);
        factory.setUsername("admin");
        factory.setPassword("1qazxsw2");
        factory.setAutomaticRecoveryEnabled(true);
        factory.setRequestedHeartbeat(60);
    }

    private void buildMQConnection() {
        Observable.concatDelayError(Arrays.asList(Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                connection = factory.newConnection();
                LogUtils.e("报警MQ连接成功");
                LogUtils.file("报警MQ连接成功");
                emitter.onComplete();
            }
        }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        LogUtils.e("报警MQ连接失败");
                        LogUtils.file("报警MQ连接失败");
                    }
                }), createAlarmChannel()))
                .subscribeOn(Schedulers.computation())
                .subscribe(new Observer<Object>() {
                    private Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (this.d != null && !this.d.isDisposed()) {
                            this.d.dispose();
                        }
                        this.d = d;
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogUtils.file("报警MQ连接失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Object> createAlarmChannel() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //创建一个通道
                alarmChannel = connection.createChannel();
                //处理消息的数量, 1代表一个消息收到后 不给server端反收到消息的话 不会收到下一条消息
                alarmChannel.basicQos(1);

                String dataQueueName = "spring.queue.alarmInfo";
                String id = "spring.queue.alarmInfo";
                alarmChannel.queueBind(dataQueueName, "spring.queue.tag", id);
                alarmChannel.basicConsume(dataQueueName, false,
                        new DefaultConsumer(alarmChannel) {
                            @Override
                            public void handleDelivery(String consumerTag,
                                                       Envelope envelope,
                                                       AMQP.BasicProperties properties,
                                                       byte[] body)
                                    throws IOException {

                                long deliveryTag = envelope.getDeliveryTag();

                                String message = new String(body, StandardCharsets.UTF_8);
                                LogUtils.d(message);

                                try {
                                    JSONObject jsonObject = new JSONObject(message);
                                    String deviceId = jsonObject.optString("deviceId");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                // (业务处理)
                                //给服务器回复ack包，服务器会删除消息
                                alarmChannel.basicAck(deliveryTag, true);
                            }
                        });
                LogUtils.file("createAlarmChannel Success");
                emitter.onComplete();
            }
        }).timeout(5, TimeUnit.SECONDS, Observer::onComplete);
    }
}
