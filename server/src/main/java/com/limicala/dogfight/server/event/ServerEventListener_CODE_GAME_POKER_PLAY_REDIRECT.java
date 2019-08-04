package com.limicala.dogfight.server.event;

import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.server.ServerContains;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerEventListener_CODE_GAME_POKER_PLAY_REDIRECT implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {
		Room room = ServerContains.getRoom(clientSide.getRoomId());
		
		List<Map<String, Object>> clientInfos = new ArrayList<Map<String,Object>>(3);
		for(ClientSide client: room.getClientSideList()){
			if(clientSide.getId() != client.getId()){
				clientInfos.add(MapHelper.newInstance()
						.put("clientId", client.getId())
						.put("clientNickname", client.getNickname())
						//.put("type", client.getType())
						.put("surplus", client.getPokers().size())
						.put("position", clientSide.getPre().getId() == client.getId() ? "UP" : "DOWN")
						.map());
			}
		}
		
		String result = MapHelper.newInstance()
				.put("pokers", clientSide.getPokers())
				.put("clientInfos", clientInfos)
				.put("sellClientId", room.getCurrentSellClient())
				.put("sellClientNickname", ServerContains.CLIENT_SIDE_MAP.get(room.getCurrentSellClient()).getNickname())
				.json();
		
		ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_REDIRECT, result);
	}

}
