package com.limicala.dogfight.server.handler;

import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import com.limicala.dogfight.entity.ServerTransferData.ServerTransferDataProtoc;

import java.util.List;

public class SecondProtobufCodec extends MessageToMessageCodec<ServerTransferDataProtoc, MessageLite> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLite msg, List<Object> out) throws Exception {
		out.add(msg);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ServerTransferDataProtoc msg, List<Object> out) throws Exception {
		out.add(msg);
	}


}
