package com.limicala.dogfight.server.event;

import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.enums.ServerEventCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public interface ServerEventListener {

	public void call(ClientSide client, String data);

	public final static Map<ServerEventCode, ServerEventListener> LISTENER_MAP = new HashMap<>();
	
	final static String LISTENER_PREFIX = "com.limicala.dogfight.server.event.ServerEventListener_";
	
	public static ServerEventListener get(ServerEventCode code){
		ServerEventListener listener = null;
		try {
			if(ServerEventListener.LISTENER_MAP.containsKey(code)){
				listener = ServerEventListener.LISTENER_MAP.get(code);
			}else{
				String eventListener = LISTENER_PREFIX + code.name();
				Class<ServerEventListener> listenerClass = (Class<ServerEventListener>) Class.forName(eventListener);
				try {
					listener = listenerClass.getDeclaredConstructor().newInstance();
				} catch (InvocationTargetException | NoSuchMethodException e) {
					e.printStackTrace();
				}
				ServerEventListener.LISTENER_MAP.put(code, listener);
			}
			return listener;
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return listener;
	}
	
}
