package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_FAIL_BY_FULL extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		Map<String, Object> dataMap = MapHelper.parser(data);
		
		SimplePrinter.printNotice("Join room failed. Room " + dataMap.get("roomId") + " player count is full!");
		ClientEventListener.get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}



}
