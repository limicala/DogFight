package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import com.limicala.dogfight.client.SimpleClient;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.Map;

public class ClientEventListener_CODE_CLIENT_EXIT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		Map<String, Object> map = MapHelper.parser(data);
		
		Integer exitClientId = (Integer) map.get("exitClientId");
		
		String role = null;
		if(exitClientId == SimpleClient.id) {
			role = "You";
		}else {
			role = String.valueOf(map.get("exitClientNickname"));
		}
		SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.CLIENT_EXIT, role));
		
		get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}

}
