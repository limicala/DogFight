package com.limicala.dogfight.server;

import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.ClientRole;
import com.limicala.dogfight.print.SimplePrinter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import com.limicala.dogfight.entity.ServerTransferData.ServerTransferDataProtoc;
import com.limicala.dogfight.enums.ClientStatus;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.server.event.ServerEventListener;

public class TransferHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();
		
		//init client info
		ClientSide clientSide = new ClientSide(getId(ctx.channel()), ClientStatus.TO_CHOOSE, ch);
		clientSide.setNickname(String.valueOf(clientSide.getId()));
		clientSide.setRole(ClientRole.PLAYER);
		
		ServerContains.CLIENT_SIDE_MAP.put(clientSide.getId(), clientSide);
		SimplePrinter.serverLog("Has client connect to the server：" + clientSide.getId());
		
		ChannelUtils.pushToClient(ch, ClientEventCode.CODE_CLIENT_CONNECT, String.valueOf(clientSide.getId()));
		ChannelUtils.pushToClient(ch, ClientEventCode.CODE_CLIENT_NICKNAME_SET, null);
	}

	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof ServerTransferDataProtoc) {
			ServerTransferDataProtoc serverTransferData = (ServerTransferDataProtoc) msg;
			ServerEventCode code = ServerEventCode.valueOf(serverTransferData.getCode());
			if(code != null && code != ServerEventCode.CODE_CLIENT_HEAD_BEAT) {
				ClientSide client = ServerContains.CLIENT_SIDE_MAP.get(getId(ctx.channel()));
				SimplePrinter.serverLog(client.getId() + " | " + client.getNickname() + " do:" + code.getMsg());
				ServerEventListener.get(code).call(client, serverTransferData.getData());
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(cause instanceof java.io.IOException) {
			clientOfflineEvent(ctx.channel());
		}else {
			SimplePrinter.serverLog("ERROR：" + cause.getMessage());
			cause.printStackTrace();
		}
	}
	
    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
        if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.READER_IDLE) {  
                try{
                	clientOfflineEvent(ctx.channel());
                	ctx.channel().close();
                }catch(Exception e){
                }
            }  
        } else {  
            super.userEventTriggered(ctx, evt);  
        }  
    }  
	
    private int getId(Channel channel){
    	String longId = channel.id().asLongText();
    	Integer clientId = ServerContains.CHANNEL_ID_MAP.get(longId);
    	if(null == clientId){
    		clientId = ServerContains.getClientId();
    		ServerContains.CHANNEL_ID_MAP.put(longId, clientId);
    	}
    	return clientId;
    }
    
    private void clientOfflineEvent(Channel channel){
    	int clientId = getId(channel);
    	ClientSide client = ServerContains.CLIENT_SIDE_MAP.get(clientId);
    	if(client != null) {
			SimplePrinter.serverLog("Has client exit to the server：" + clientId + " | " + client.getNickname());
			ServerEventListener.get(ServerEventCode.CODE_CLIENT_OFFLINE).call(client, null);
		}
    }
    
}
