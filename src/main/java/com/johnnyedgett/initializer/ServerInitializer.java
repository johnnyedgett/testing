package com.johnnyedgett.initializer;

import com.johnnyedgett.handler.HttpRequestHandler;
import com.johnnyedgett.handler.TextWebSocketFrameHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
        pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(64 * 1024));
        pipeline.addLast("httpHandler", new HttpRequestHandler("/ws"));

        // Handles upgrade handshake, Ping/Pong/Close WebsocketFrames
        // Replaces Http en/decoders with Websocket en/decoders upon upgrade
        pipeline.addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws1"));
        pipeline.addLast("textWebSocketFrameHandler", new TextWebSocketFrameHandler());
    }
}
