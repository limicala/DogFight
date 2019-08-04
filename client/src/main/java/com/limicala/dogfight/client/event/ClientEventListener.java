package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.ServerEventCode;

import java.util.HashMap;
import java.util.Map;

public abstract class ClientEventListener {

	public abstract void call(Channel channel, String data);

	public final static Map<ClientEventCode, ClientEventListener> LISTENER_MAP = new HashMap<>();
	
	private final static String LISTENER_PREFIX = "com.limicala.dogfight.client.event.ClientEventListener_";
	
	public static ClientEventListener get(ClientEventCode code){
		ClientEventListener listener = null;
		try {
			if(ClientEventListener.LISTENER_MAP.containsKey(code)){
				listener = ClientEventListener.LISTENER_MAP.get(code);
			}else{
				String eventListener = LISTENER_PREFIX + code.name();
				Class<ClientEventListener> listenerClass = (Class<ClientEventListener>) Class.forName(eventListener);
				listener = listenerClass.newInstance();
				ClientEventListener.LISTENER_MAP.put(code, listener);
			}
			return listener;
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return listener;
	}
	
	protected ChannelFuture pushToServer(Channel channel, ServerEventCode code, String datas){
		return ChannelUtils.pushToServer(channel, code, datas);
	}
	
	protected ChannelFuture pushToServer(Channel channel, ServerEventCode code){
		return pushToServer(channel, code, null);
	}
}
