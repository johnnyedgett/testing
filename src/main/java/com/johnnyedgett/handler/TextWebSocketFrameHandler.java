package com.johnnyedgett.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // do nothing right now
        log.info(textWebSocketFrame.text());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) throws Exception {
        if(event instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("Connected");
            context.pipeline().remove(HttpRequestHandler.class);
        } else {
            super.userEventTriggered(context, event);
        }
    }
}
