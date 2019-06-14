package com.qintingfm.netty.tcp2tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class Tcp2tcpResponse extends ChannelHandlerAdapter {
    ChannelHandlerContext clientCtx;
    public Tcp2tcpResponse(ChannelHandlerContext clientCtx) {
        this.clientCtx=clientCtx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf msg1 = (ByteBuf) msg;
        ByteBuf duplicate = msg1.duplicate();
        ChannelFuture channelFuture = clientCtx.writeAndFlush(duplicate);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("send msg to client");
            }
        });
//        ReferenceCountUtil.release(msg1);
//        ReferenceCountUtil.release(duplicate);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("connnect server");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("disconnect server");
    }
}
