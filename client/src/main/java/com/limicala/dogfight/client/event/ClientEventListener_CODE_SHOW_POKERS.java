package com.limicala.dogfight.client.event;

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
		
		SimplePrinter.printNotice(map.get("clientNickname") + " played:");
		
		List<Poker> pokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {});
		SimplePrinter.printPokers(pokers);
		
		if(map.containsKey("sellClinetNickname")) {
			SimplePrinter.printNotice("Next player is " + map.get("sellClinetNickname") + ". Please wait for him to play his pokers.");
		}
	}

}
