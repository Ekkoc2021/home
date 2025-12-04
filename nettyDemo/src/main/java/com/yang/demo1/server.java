package com.yang.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.SocketAddress;
import java.util.concurrent.ExecutionException;

public class server {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 链式编程:配置服务器端,线程,pipeline,端口
       ServerBootstrap server=new ServerBootstrap()
                .group(new NioEventLoopGroup()) // boss和worker共用一个事件循环组,构造方法默认是1个线程池
                .channel(NioServerSocketChannel.class) // 配置创建服务端socket类型
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        // 具体业务handler
                        ch.pipeline().addLast(new FixedLengthFrameDecoder(19));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                System.out.println(ctx.alloc().getClass());
                                System.out.println(msg);
                                ByteBuf buffer = ctx.alloc().buffer(64);

                                if (buffer.isDirect()) {
                                    System.out.println("直接内存");
                                } else {
                                    System.out.println("堆内存");
                                }

                            }
                        });
                    }
                });
        ChannelFuture bind = server.bind(8080);


        ChannelFuture sync = bind.sync(); // 等待服务器启动完成
//        Void unused = sync.get();
//        System.out.println(unused.getClass());
        sync.addListener(new ChannelFutureListener(){
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println(Thread.currentThread().getName());
                if (future.isSuccess()) {
                    System.out.println("服务器启动成功");
                } else {
                    System.out.println("服务器启动失败");
                }
            }
        });

        Channel channel = sync.channel(); // 获取到对应的ServerSocketChannel,有时候需要获取绑定成功后的channel的一些信息
        System.out.println(channel.localAddress());
        System.out.println(channel.pipeline());
        // 添加关闭监听器
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                // 服务器关闭时执行
                System.out.println("服务器已关闭");
                // 通常在这里关闭EventLoopGroup，以释放资源
                future.channel().eventLoop().shutdownGracefully();
            }
        });


    }

    class a extends  ChannelHandlerAdapter{

    }
}
