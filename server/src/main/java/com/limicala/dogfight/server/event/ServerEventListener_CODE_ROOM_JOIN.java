package com.limicala.dogfight.server.event;

import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.RoomStatus;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.server.ServerContains;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentSkipListMap;

public class ServerEventListener_CODE_ROOM_JOIN implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {

		Room room = ServerContains.getRoom(Integer.valueOf(data));
		
		if(room == null) {
			String result = MapHelper.newInstance()
								.put("roomId", data)
								.json();
			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_INEXIST, result);
		}else {
			if(room.getClientSideList().size() == 3) {
				String result = MapHelper.newInstance()
						.put("roomId", room.getId())
						.put("roomOwner", room.getRoomOwner())
						.json();
				ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_FULL, result);
			}else {
				clientSide.setRoomId(room.getId());

				ConcurrentSkipListMap<Integer, ClientSide> roomClientMap = (ConcurrentSkipListMap<Integer, ClientSide>) room.getClientSideMap();
				LinkedList<ClientSide> roomClientList = room.getClientSideList();

				if(roomClientList.size() > 0){
					ClientSide pre = roomClientList.getLast();
					pre.setNext(clientSide);
					clientSide.setPre(pre);
				}

				roomClientList.add(clientSide);
				roomClientMap.put(clientSide.getId(), clientSide);

				if(roomClientMap.size() == 3) {
					clientSide.setNext(roomClientList.getFirst());
					roomClientList.getFirst().setPre(clientSide);

					ServerEventListener.get(ServerEventCode.CODE_GAME_STARTING).call(clientSide, String.valueOf(room.getId()));
				}else {
					room.setStatus(RoomStatus.WAIT);

					String result = MapHelper.newInstance()
							.put("clientId", clientSide.getId())
							.put("clientNickname", clientSide.getNickname())
							.put("roomId", room.getId())
							.put("roomOwner", room.getRoomOwner())
							.put("roomClientCount", room.getClientSideList().size())
							.json();
					for(ClientSide client: roomClientMap.values()) {
						ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_ROOM_JOIN_SUCCESS, result);
					}
				}
			}


		}
	}





}
