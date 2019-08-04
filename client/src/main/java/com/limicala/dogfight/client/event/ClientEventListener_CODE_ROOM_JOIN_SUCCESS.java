package com.limicala.dogfight.client.event;

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
			SimplePrinter.printNotice("You have joined room：" + map.get("roomId") + ". There are " + map.get("roomClientCount") + " players in the room now.");
			SimplePrinter.printNotice("Please wait for other players to join, start a good game when the room player reaches three !");
		}else {
			SimplePrinter.printNotice(map.get("clientNickname") + " joined room, the current number of room player is " + map.get("roomClientCount"));
		}
		
		
		
	}



}
