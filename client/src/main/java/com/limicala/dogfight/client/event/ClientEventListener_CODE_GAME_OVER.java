package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.Map;

public class ClientEventListener_CODE_GAME_OVER extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		Map<String, Object> map = MapHelper.parser(data);
		SimplePrinter.printNotice(map.get("winnerNickname") + " lost");
		SimplePrinter.printNotice("Game over");
	}

}
