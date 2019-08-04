package com.limicala.dogfight.client.event;

import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.print.SimplePrinter;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_GAME_POKER_PLAY_MISMATCH_MIN extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice("mismatch min pokerSell");
		pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
