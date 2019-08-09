package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.print.SimplePrinter;

public class ClientEventListener_CODE_PVE_DIFFICULTY_NOT_SUPPORT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice(ConsoleMessage.PVE_DIFFICULTY_NOT_SUPPORT);
		get(ClientEventCode.CODE_SHOW_OPTIONS_PVE).call(channel, data);
	}



}
