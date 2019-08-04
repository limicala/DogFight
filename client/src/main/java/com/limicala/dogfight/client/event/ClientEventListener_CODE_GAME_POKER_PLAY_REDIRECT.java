package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.client.SimpleClient;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.FormatPrinter;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.List;
import java.util.Map;

import static com.limicala.dogfight.client.event.ClientEventListener_CODE_CLIENT_NICKNAME_SET.NICKNAME_MAX_LENGTH;

public class ClientEventListener_CODE_GAME_POKER_PLAY_REDIRECT extends ClientEventListener{

	private static String[] choose = new String[]{"UP", "DOWN"};
	
	private static String format = "\n[%-4s]%-" + NICKNAME_MAX_LENGTH + "ssurplus %-2s [%-8s]";
	
	@Override
	public void call(Channel channel, String data) {
		
		Map<String, Object> map = MapHelper.parser(data);
		
		int sellClientId = (int) map.get("sellClientId");
		
		List<Map<String, Object>> clientInfos = (List<Map<String, Object>>) map.get("clientInfos");
		
		for(int index = 0; index < 2; index ++){
			for(Map<String, Object> clientInfo: clientInfos) {
				String position = (String) clientInfo.get("position");
				if(position.equalsIgnoreCase(choose[index])){
					FormatPrinter.printNotice(format, clientInfo.get("position"), clientInfo.get("clientNickname"), clientInfo.get("surplus"), "normal");
				}
			}
		}
		SimplePrinter.printNotice("");
		
		if(sellClientId == SimpleClient.id) {
			get(ClientEventCode.CODE_GAME_POKER_PLAY).call(channel, data);
		}else {
			SimplePrinter.printNotice("Next player is " + map.get("sellClientNickname") + ". Please wait for him to play his cards.");
		}
	}

}
