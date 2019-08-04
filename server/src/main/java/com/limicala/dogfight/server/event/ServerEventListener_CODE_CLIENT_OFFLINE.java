package com.limicala.dogfight.server.event;

import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.ClientRole;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.server.ServerContains;

public class ServerEventListener_CODE_CLIENT_OFFLINE implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {
		
		Room room = ServerContains.getRoom(clientSide.getRoomId());

		if(room != null) {
			String result = MapHelper.newInstance()
								.put("roomId", room.getId())
								.put("exitClientId", clientSide.getId())
								.put("exitClientNickname", clientSide.getNickname())
								.json();
			for(ClientSide client: room.getClientSideList()) {
				if(client.getRole() == ClientRole.PLAYER){
					if(client.getId() != clientSide.getId()){
						ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_CLIENT_EXIT, result);
						client.init();
					}
				}
			}
			ServerContains.removeRoom(room.getId());
		}
		
		ServerContains.CLIENT_SIDE_MAP.remove(clientSide.getId());
	}

}
