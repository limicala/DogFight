package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.print.SimplePrinter;

public class ClientEventListener_CODE_ROOM_CREATE_SUCCESS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		
		Room room = Noson.convert(data, Room.class);
		
		SimplePrinter.printNotice(ConsoleMessage.bind(ConsoleMessage.ROOM_CREATE_SUCCESS, room.getId()));
	}

}
