package com.limicala.dogfight.server.event;

import org.nico.noson.Noson;
import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.server.ServerContains;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ServerEventListener_CODE_GET_ROOMS implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {
		List<Map<String, Object>> roomList = new ArrayList<>(ServerContains.getRoomMap().size());
		for(Entry<Integer, Room> entry: ServerContains.getRoomMap().entrySet()) {
			Room room = entry.getValue();
			roomList.add(MapHelper.newInstance()
					.put("roomId", room.getId())
					.put("roomOwner", room.getRoomOwner())
					.put("roomClientCount", room.getClientSideList().size())
					.map());
		}
		ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_SHOW_ROOMS, Noson.reversal(roomList));
	}

}
