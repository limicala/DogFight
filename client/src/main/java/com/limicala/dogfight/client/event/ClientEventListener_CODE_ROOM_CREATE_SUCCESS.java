package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import org.nico.noson.Noson;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.print.SimplePrinter;

public class ClientEventListener_CODE_ROOM_CREATE_SUCCESS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		
		Room room = Noson.convert(data, Room.class);
		
		SimplePrinter.printNotice("You have created a room with id " + room.getId());
		SimplePrinter.printNotice("Please wait for other players to join !");
	}

}
