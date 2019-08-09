package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_FAIL_BY_FULL extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		Map<String, Object> dataMap = MapHelper.parser(data);
		
		SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.ROOM_JOIN_FAIL_BY_FULL, dataMap.get("roomId")));
		ClientEventListener.get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}



}
