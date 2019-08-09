package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import com.limicala.dogfight.entity.Poker;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_SHOW_POKERS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		
		Map<String, Object> map = MapHelper.parser(data);
		
		SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.SHOW_POKERS_PLAY, map.get("clientNickname")));
		
		List<Poker> pokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {});
		SimplePrinter.printPokers(pokers);
		
		if(map.containsKey("sellClientNickname")) {
			SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.SHOW_POKERS_NEXT_PLAY, map.get("sellClientNickname")));
		}
	}

}
