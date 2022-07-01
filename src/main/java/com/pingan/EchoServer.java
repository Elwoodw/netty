package com.pingan;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private  final  int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws  Exception {
        int port=Integer.valueOf(args[0]);
        new EchoServer(port).start();

    }

    public  void start() throws Exception{
        final EchoServerHandler handler=new EchoServerHandler();
        EventLoopGroup group=new NioEventLoopGroup();

        ServerBootstrap b=new ServerBootstrap();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(handler);
                    }
                });
        try {
            ChannelFuture f=b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }


    }
}
