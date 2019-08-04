package com.limicala.dogfight.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import com.limicala.dogfight.entity.ServerTransferData;

import java.util.concurrent.TimeUnit;

public class DefaultChannelInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ch.pipeline()
		.addLast(new IdleStateHandler(300, 0, 0, TimeUnit.SECONDS))
        .addLast(new ProtobufVarint32FrameDecoder())
        .addLast(new ProtobufDecoder(ServerTransferData.ServerTransferDataProtoc.getDefaultInstance()))
        .addLast(new ProtobufVarint32LengthFieldPrepender())
        .addLast(new ProtobufEncoder())
        .addLast(new SecondProtobufCodec())
        .addLast(new TransferHandler());
		
	}

}
