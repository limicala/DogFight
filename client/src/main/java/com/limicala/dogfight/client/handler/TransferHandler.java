package com.limicala.dogfight.client.handler;

import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientTransferData;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.print.SimplePrinter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import com.limicala.dogfight.client.event.ClientEventListener;
import com.limicala.dogfight.enums.ServerEventCode;

public class TransferHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if(msg instanceof ClientTransferData.ClientTransferDataProtoc) {
			ClientTransferData.ClientTransferDataProtoc clientTransferData = (ClientTransferData.ClientTransferDataProtoc) msg;
			if(clientTransferData.getInfo() != null && ! clientTransferData.getInfo().isEmpty()) {
				SimplePrinter.printNotice(clientTransferData.getInfo());
			}
			ClientEventCode code = ClientEventCode.valueOf(clientTransferData.getCode());
			if(code != null) {
				ClientEventListener.get(code).call(ctx.channel(), clientTransferData.getData());
			}
		}
	}

	@Override  
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
		if (evt instanceof IdleStateEvent) {  
			IdleStateEvent event = (IdleStateEvent) evt;  
			if (event.state() == IdleState.WRITER_IDLE) {  
				ChannelUtils.pushToServer(ctx.channel(), ServerEventCode.CODE_CLIENT_HEAD_BEAT, "heartbeat");
			}  
		}  
	} 
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(cause instanceof java.io.IOException) {
			SimplePrinter.printNotice("The network is not good or did not operate for a long time, has been offline");
			System.exit(0);
		}
	}

}
