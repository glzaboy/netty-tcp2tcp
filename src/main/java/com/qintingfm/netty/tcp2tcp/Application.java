package com.qintingfm.netty.tcp2tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Application {




    public static void main(String[] args) {
        NioEventLoopGroup boss=new NioEventLoopGroup();
        NioEventLoopGroup work=new NioEventLoopGroup();


        ServerBootstrap serverBootstrap=new ServerBootstrap();
        serverBootstrap.group(boss,work);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
        serverBootstrap.option(ChannelOption.SO_BACKLOG,128);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new Tcp2tcp());
//                ch.pipeline().addLast(new ReadTimeoutHandler(5));--增加超时自动断开链接
            }
        });
        try {
            ChannelFuture sync = serverBootstrap.bind(9092).sync();//--本地监听端口
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
