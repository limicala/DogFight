package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_MISMATCH extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		Map<String, Object> map = MapHelper.parser(data);
		
		SimplePrinter.printNotice("Your pokers' type is " + map.get("playType") + " (" + map.get("playCount") + ") but previous pokers' type is " + map.get("preType") + " (" + map.get("preCount") + "), mismatch !!");
		
		pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
