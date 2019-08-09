package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import com.limicala.dogfight.entity.Poker;
import com.limicala.dogfight.enums.PokerLevel;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.print.SimpleWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		Map<String, Object> map = MapHelper.parser(data);
		
		SimplePrinter.printNotice(ConsoleMessage.PLAY_YOUR_TURN);
		List<Poker> pokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {});
		SimplePrinter.printPokers(pokers);
		
		
		SimplePrinter.printNotice(ConsoleMessage.PLAY_ENTER_CARD);
		String line = SimpleWriter.write("card");

		if(line == null){
			SimplePrinter.printNotice(ConsoleMessage.INVALID_ENTER);
			call(channel, data);
		}else{
			if(line.equalsIgnoreCase("PASS")) {
				pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_PASS);
			}else if(line.equalsIgnoreCase("EXIT")){
				pushToServer(channel, ServerEventCode.CODE_CLIENT_EXIT);
			}else {
				String[] strs = line.split(" ");
				List<Character> options = new ArrayList<>();
				boolean access = true;
				for(int index = 0; index < strs.length; index ++){
					String str = strs[index];
					for(char c: str.toCharArray()) {
						if(c == ' ' || c == '\t') {
						}else {
							if(! PokerLevel.aliasContains(c)) {
								access = false;
								break;
							}else {
								options.add(c);
							}
						}
					}
				}
				if(access){
					pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY, Noson.reversal(options.toArray(new Character[] {})));
				}else{
					SimplePrinter.printNotice(ConsoleMessage.INVALID_ENTER);
					call(channel, data);
				}
			}
		}
		
	}

}
