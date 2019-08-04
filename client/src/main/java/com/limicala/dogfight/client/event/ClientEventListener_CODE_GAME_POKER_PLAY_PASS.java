package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.client.SimpleClient;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_PASS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		Map<String, Object> map = MapHelper.parser(data);
		
		SimplePrinter.printNotice(map.get("clientNickname") + " passed. It is now " + map.get("nextClientNickname") + "'s turn.");
		
		int turnClientId = (int) map.get("nextClientId");
		if(SimpleClient.id == turnClientId) {
			pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
		}
	}

}
