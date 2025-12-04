package com.yang.rkmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class provider {
    @Test
    public void test01() throws Exception {
// 声明group
        DefaultMQProducer producer = new DefaultMQProducer("group_test");

        // 声明namesrv地址
        producer.setNamesrvAddr("192.168.169.223:9876");
        producer.setRetryTimesWhenSendFailed(2);

        // 启动实例
        producer.start();


        // 设置消息的topic,tag以及消息体
        Message msg = new Message("topic_test", "tag_test", "消息内33容".getBytes(StandardCharsets.UTF_8));


        // 发送消息，并设置10s连接超时
        SendResult send = producer.send(msg, 10000);
        System.out.println("发送结果：" + send);

        // 关闭实例
        producer.shutdown();
    }

    // 发送普通消息
    @Test
    public void test02() throws Exception {
        DefaultMQProducer defaultMQProducer = initDefaultProduer();
        Message msg = new Message("topic_test01", "tags233", "hello3 3world2".getBytes(StandardCharsets.UTF_8));
        SendResult send = defaultMQProducer.send(msg, 10000); // 发送会等broker响应后才进行下一步操作
        System.out.println(send);

        // 单向消息
        msg.setBody("单向消息:不管成功发送与否".getBytes());
        defaultMQProducer.sendOneway(msg);

        // 批量消息
        List<Message> messages=new LinkedList<>();
        Message msg2 = new Message("topic_test01", "tags233", "批量消息1".getBytes(StandardCharsets.UTF_8));
        Message msg3 = new Message("topic_test01", "tags233", "批量消息2".getBytes(StandardCharsets.UTF_8));
        messages.add(msg2);
        messages.add(msg3);
        SendResult send2 = defaultMQProducer.send(messages, 10000);
        System.out.println(send2);
    }

    // 顺序消息
    @Test
    public void test03() throws Exception {
        DefaultMQProducer defaultMQProducer = initDefaultProduer();
        Message msg = new Message("topic_test01", "tags233", "-1".getBytes(StandardCharsets.UTF_8));

        for (int i = 0; i < 10; i++) {
            SendResult send = defaultMQProducer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    // 固定选择queueId=0的队列
                    for (MessageQueue mq : mqs) {
                        System.out.println(mq.getQueueId());
                        if (mq.getQueueId() == 3) {
                            return mq;
                        }
                    }
                    // 如果没有queueId=0，返回第一个队列
                    return mqs.get(0);
                }
            }, null);
            System.out.println(send);
            String body = i + "";
            msg.setBody(body.getBytes());
        }
    }

    // 延迟消息
    @Test
    public void test04() throws Exception {
        DefaultMQProducer defaultMQProducer = initDefaultProduer();
        Message msg = new Message("topic_test02", "第三次", "-1".getBytes(StandardCharsets.UTF_8));
        // messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        for (int i = 0; i < 10; i++) {
            String body = System.currentTimeMillis() + "";
            msg.setBody(body.getBytes());
            msg.setDelayTimeLevel(2);
            SendResult send = defaultMQProducer.send(msg);
            System.out.println(send);
            Thread.sleep(1000);

        }

    }

    private static DefaultMQProducer initDefaultProduer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("group_test");

        producer.setNamesrvAddr("192.168.169.223:9876");
        producer.start();
        return producer;
    }

    // 事务消息：
    static class myTransactionListener implements TransactionListener {
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            System.out.println("业务参数（根据业务参数执行不同业务类型）： "+arg+ "执行事务 : " + new String(msg.getBody()));
            // 模拟业务逻辑
            double random = Math.random();
            if (random > 0.5) {
                System.out.println("事务执行完毕：消息正常发送。");
                return LocalTransactionState.COMMIT_MESSAGE;
            } else if (random > 0.3) {
                System.out.println("本地能检查到执行失败，主动回滚。");
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
            // 异常:执行异常rocketmq会帮助捕获异常，然后通知broker
            return null;
        }

        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            System.out.println("事务回查 : " + new String(msg.getBody()));
            try {
                // 检查本地事务状态
                boolean transactionCommitted = Math.random() > 0.3;

                if (transactionCommitted) {
                    // 重新提交事务
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                // 执行失败回滚
                return LocalTransactionState.ROLLBACK_MESSAGE;
            } catch (Exception e) {
                System.err.println("回查异常: " + e.getMessage());
                return LocalTransactionState.UNKNOW;
            }
        }
    }

    @Test
    public void test05() throws Exception {
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer("Transaction_group_test05");
        transactionMQProducer.setNamesrvAddr("192.168.169.223:9876");

        transactionMQProducer.setTransactionListener(new myTransactionListener());

        transactionMQProducer.start();

        for (int i = 0; i < 10; i++) {
            String body="事务消息 "+i;
            Message message = new Message("topic_test03", "tags233",  body.getBytes(StandardCharsets.UTF_8));

            TransactionSendResult sendResult = transactionMQProducer.sendMessageInTransaction(message, "business_arg");

            System.out.println("发送结果: " + sendResult.getSendStatus());
            System.out.println("事务状态: " + sendResult.getLocalTransactionState());
            System.out.println("消息ID: " + sendResult.getMsgId());
        }

         transactionMQProducer.shutdown();
    }

    // 检查事务：同一个消费者组帮助兜底
    @Test
    public void test06() throws Exception {
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer("Transaction_group_test05");
        transactionMQProducer.setTransactionListener(new myTransactionListener());
        transactionMQProducer.setNamesrvAddr("192.168.169.223:9876");
        transactionMQProducer.start();
        String body="回查生产者 事务消息 ";
        Message message = new Message("topic_test03", "tags233",  body.getBytes(StandardCharsets.UTF_8));

        TransactionSendResult sendResult = transactionMQProducer.sendMessageInTransaction(message, "business_arg");

        System.out.println("发送结果: " + sendResult.getSendStatus());
        System.out.println("事务状态: " + sendResult.getLocalTransactionState());
        System.out.println("消息ID: " + sendResult.getMsgId());
        while (true) {
            Thread.sleep(50000);
        }

    }


    // 死信消息
    @Test
    public void test07() throws Exception {
        DefaultMQProducer defaultMQProducer = initDefaultProduer();
        Message msg = new Message("topic_test01", "死信消息模拟", "-1".getBytes(StandardCharsets.UTF_8));

        for (int i = 0; i < 10; i++) {
            SendResult send = defaultMQProducer.send(msg);
            System.out.println(send);
            String body = i + "";
            msg.setBody(body.getBytes());
        }
    }

}
