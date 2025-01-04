package com.johnnyedgett;

import com.johnnyedgett.initializer.ServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        if(args.length != 1)
            throw new IllegalArgumentException("Expected a port number to be provided");

        Server s = new Server(Integer.parseInt(args[0]));
        ChannelFuture future = s.start();
        future.channel().closeFuture().syncUninterruptibly();
    }

    public ChannelFuture start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ServerInitializer());

        ChannelFuture channelFuture = bootstrap.bind(port);
        channelFuture.syncUninterruptibly();

        log.info("Starting Server on port {}", port);
        return channelFuture;
    }
}