package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.client.SimpleClient;
import com.limicala.dogfight.print.SimplePrinter;

public class ClientEventListener_CODE_CLIENT_CONNECT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice("Connection to server is successful. Welcome to ratel!!");
		SimpleClient.id = Integer.parseInt(data);
	}

}
