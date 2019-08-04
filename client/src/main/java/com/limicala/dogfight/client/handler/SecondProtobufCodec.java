package com.limicala.dogfight.client.handler;

import com.google.protobuf.MessageLite;
import com.limicala.dogfight.entity.ClientTransferData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class SecondProtobufCodec extends MessageToMessageCodec<ClientTransferData.ClientTransferDataProtoc, MessageLite> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLite msg, List<Object> out) throws Exception {
		out.add(msg);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ClientTransferData.ClientTransferDataProtoc msg, List<Object> out) throws Exception {
		out.add(msg);
	}

}
