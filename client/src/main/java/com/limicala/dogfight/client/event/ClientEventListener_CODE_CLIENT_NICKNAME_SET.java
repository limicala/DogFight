package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import org.nico.noson.util.string.StringUtils;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.print.SimpleWriter;

import java.util.Map;

public class ClientEventListener_CODE_CLIENT_NICKNAME_SET extends ClientEventListener{

	public static final int NICKNAME_MAX_LENGTH = 10;
	
	@Override
	public void call(Channel channel, String data) {
		
		// If it is not the first time that the user is prompted to enter nickname
		// If first time, data = null or "" otherwise not empty
		if (StringUtils.isNotBlank(data)) {
			Map<String, Object> dataMap = MapHelper.parser(data);
			if (dataMap.containsKey("invalidLength")) {
				SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.NICKNAME_INVALID, dataMap.get("invalidLength")));
			}
		}
		SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.NICKNAME_SET, NICKNAME_MAX_LENGTH));
		String nickname = SimpleWriter.write("nickname");
		
		// If the length of nickname is more that NICKNAME_MAX_LENGTH
		if (nickname.trim().length() > NICKNAME_MAX_LENGTH) {
			String result = MapHelper.newInstance().put("invalidLength", nickname.trim().length()).json();
			get(ClientEventCode.CODE_CLIENT_NICKNAME_SET).call(channel, result);
		}else{
			pushToServer(channel, ServerEventCode.CODE_CLIENT_NICKNAME_SET, nickname);
		}
	}



}
