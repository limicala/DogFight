package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import com.limicala.dogfight.client.SimpleClient;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_SUCCESS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		Map<String, Object> map = MapHelper.parser(data);
		
		int joinClientId = (int) map.get("clientId");
		if(SimpleClient.id == joinClientId) {
			SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.ROOM_JOIN_SUCCESS, map.get("roomId"), map.get("roomClientCount")));
		}else {
			SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.ROOM_JOIN_SUCCESS2, map.get("clientNickname"), map.get("roomClientCount")));
		}
		
		
		
	}



}
