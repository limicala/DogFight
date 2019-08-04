package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.print.SimplePrinter;

public class ClientEventListener_CODE_CLIENT_KICK extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		
		SimplePrinter.printNotice("As a result of long time do not operate, be forced by the system to kick out of the room\n");
		
		get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}

}
