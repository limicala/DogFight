package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.print.SimplePrinter;

public class ClientEventListener_CODE_ROOM_PLAY_FAIL_BY_INEXIST extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		
		SimplePrinter.printNotice("Play failed. Room already dissolution!");
		ClientEventListener.get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
	}



}
