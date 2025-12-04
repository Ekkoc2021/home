package com.yang.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

public class client {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap client=new Bootstrap()
                .group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });

        ChannelFuture connect = client.connect("127.0.0.1", 8080);
        ChannelFuture sync = connect.sync(); // 阻塞等待连接建立成功
        Channel channel = sync.channel(); // 连接成功后获取channel对象
        channel.writeAndFlush(new Date() + ": hello world!"); // 写入数据并刷新
        channel.closeFuture().addListener(future -> {
            System.out.println("客户端关闭");
        });
        System.out.println(channel.localAddress());
        channel.close();
//        eventExecutors.shutdownGracefully(); // 优雅关闭线程池,group内部的线程是非守护线程
    }
}
