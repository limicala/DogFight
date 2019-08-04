package com.limicala.dogfight.server.event;

import org.nico.noson.Noson;
import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.RoomStatus;
import com.limicala.dogfight.enums.RoomType;
import com.limicala.dogfight.server.ServerContains;

public class ServerEventListener_CODE_ROOM_CREATE implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {
		
		Room room = new Room(ServerContains.getServerId());
		room.setStatus(RoomStatus.BLANK);
		room.setType(RoomType.PVP);
		room.setRoomOwner(clientSide.getNickname());
		room.getClientSideMap().put(clientSide.getId(), clientSide);
		room.getClientSideList().add(clientSide);
		room.setLastFlushTime(System.currentTimeMillis());
		
		clientSide.setRoomId(room.getId());
		ServerContains.addRoom(room);
		
		ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_CREATE_SUCCESS, Noson.reversal(room));
	}

	



}
