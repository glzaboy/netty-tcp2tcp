package com.qintingfm.netty.tcp2tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Tcp2tcp extends ChannelHandlerAdapter {
    NioEventLoopGroup connectWork;
    Bootstrap bootstrap;
    ChannelFuture connectChannel;
    public Tcp2tcp() throws UnknownHostException, InterruptedException {
        connectWork=new NioEventLoopGroup();
        bootstrap=new Bootstrap();
        bootstrap.group(connectWork);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);

    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new Tcp2tcpResponse(ctx));
            }
        });
        InetAddress inetAddress=InetAddress.getByName("172.20.20.114");//服务器ip
        connectChannel= bootstrap.connect(inetAddress, 1531).sync();//服务器端口
//        connectChannel.channel().closeFuture().sync();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        connectChannel.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf msg1 = (ByteBuf) msg;
        ChannelFuture channelFuture = connectChannel.channel().writeAndFlush(msg1);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("send msg to server");
            }
        });
//        ReferenceCountUtil.release(msg);
//        ReferenceCountUtil.release(msg1);
    }
}
