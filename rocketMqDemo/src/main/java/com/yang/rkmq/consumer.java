package com.yang.rkmq;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class consumer {

    public static final String NAMESRV_ADDR = "192.168.169.223:9876";

    @Test
    public void test01() throws Exception {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("please_rename_unique_group_name_5");
        consumer.setNamesrvAddr(NAMESRV_ADDR);
        consumer.start();

        try {
            MessageQueue mq = new MessageQueue();
            mq.setQueueId(0);
            mq.setTopic("topic_test01");
            mq.setBrokerName("broker-a");
            long offset = consumer.fetchConsumeOffset(mq, false);
            if (offset < 0) {
                offset = 0; // 如果没有消费记录，从0开始
            }
            PullResult pullResult = consumer.pull(mq, "*", offset, 32);
            // ✅ 只输出消息的body内容
            List<MessageExt> msgList = pullResult.getMsgFoundList();

            for (MessageExt msg : msgList) {
                // 将byte[]转换为字符串，使用UTF-8编码
                String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                System.out.println(body);
            }

            // 更新消费进度
            consumer.updateConsumeOffset(mq, pullResult.getNextBeginOffset());
            // 在关闭消费者之前，可以考虑强制同步到Broker
            consumer.getDefaultMQPullConsumerImpl().getOffsetStore().persist(mq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        consumer.shutdown();

    }

    // 普通消息/顺序消息
    @Test
    public void test02() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_5");
        consumer.setNamesrvAddr("192.168.169.223:9876");

        consumer.subscribe("topic_test01", "*");
        consumer.subscribe("topic_test02", "*");
        consumer.subscribe("topic_test03", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    try {
                        // 处理消息
                        String topic = msg.getTopic();
                        String tags = msg.getTags();
                        String body = new String(msg.getBody());

                        System.out.printf("Received message - Topic: %s, Tags: %s, Body: %s%n",
                                topic, tags, body);


                    } catch (Exception e) {
                        // 处理异常，返回重试
                        System.err.println("Process message failed: " + e.getMessage());
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                // 消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.println("Consumer started successfully");

        while (true){
            Thread.sleep(10000);
        }
    }

    // 测试延迟消息
    @Test
    public void test03() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_5");
        consumer.setNamesrvAddr("192.168.169.223:9876");

        consumer.subscribe("topic_test01", "*");
        consumer.subscribe("topic_test02", "*");
        consumer.subscribe("topic_test03", "*");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    try {
                        // 处理消息
                        String body = new String(msg.getBody());
                        // 消息体是一个时间戳==>输出时间戳对于的时间

                        Date now=new Date(System.currentTimeMillis());
                        Date Start=new Date(Long.parseLong(body));
                        System.out.printf("Received message - start: %s, now: %s",
                                sdf.format(Start),sdf.format(now));
                        System.out.println(" 消费完成");

                    } catch (Exception e) {
                        // 处理异常，返回重试
                        System.err.println("Process message failed: " + e.getMessage());
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                // 消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.println("Consumer started successfully");

        while (true){
            Thread.sleep(10000);
        }
    }

    // 消费失败
    @Test
    public void test04() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_5");
        consumer.setNamesrvAddr("192.168.169.223:9876");

        consumer.subscribe("topic_test01", "*");
        consumer.subscribe("topic_test02", "*");
        consumer.subscribe("topic_test03", "*");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    // 处理消息
                    String body = new String(msg.getBody());
                    System.out.println(body);
                }
                // 消费成功
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });

        consumer.start();
        System.out.println("Consumer started successfully");

        while (true){
            Thread.sleep(10000);
        }
    }
}
