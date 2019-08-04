package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.print.SimplePrinter;

public class ClientEventListener_CODE_GAME_POKER_PLAY_LESS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice("Your pokers' type has lower rank than the previous. You could not play this combination !!");
		pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
