package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.Map;

public class ClientEventListener_CODE_GAME_OVER extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		Map<String, Object> map = MapHelper.parser(data);
		SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.GAME_OVER, map.get("winnerNickname")));
	}

}
