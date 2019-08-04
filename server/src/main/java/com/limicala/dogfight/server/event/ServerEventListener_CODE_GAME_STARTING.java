package com.limicala.dogfight.server.event;

import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Poker;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.*;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.helper.PokerHelper;
import com.limicala.dogfight.server.ServerContains;
import com.limicala.dogfight.server.robot.RobotEventListener;

import java.util.LinkedList;
import java.util.List;

public class ServerEventListener_CODE_GAME_STARTING implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {

		Room room = ServerContains.getRoom(clientSide.getRoomId());

		LinkedList<ClientSide> roomClientList = room.getClientSideList();

		// Send the points of poker
		List<List<Poker>> pokersList = PokerHelper.distributePoker();
		int cursor = 0;
		for(ClientSide client: roomClientList){
			client.setPokers(pokersList.get(cursor ++));
		}
		//room.setLandlordPokers(pokersList.get(3));

		int startGrabIndex = -1;
		search:
		for (int i = 0; i < pokersList.size(); ++i){
			for (Poker poker : pokersList.get(i)){
				if (3 == poker.getLevel().getLevel() && PokerType.DIAMOND == poker.getType()){
					startGrabIndex = i;
					break search;
				}
			}
		}

		ClientSide startGrabClient = roomClientList.get(startGrabIndex);
		room.setCurrentSellClient(startGrabClient.getId());
		
		// Push start game messages
		room.setStatus(RoomStatus.STARTING);

		for(ClientSide client: roomClientList) {
			//client.setType(ClientType.PEASANT);

			String result = MapHelper.newInstance()
					.put("roomId", room.getId())
					.put("roomOwner", room.getRoomOwner())
					.put("roomClientCount", room.getClientSideList().size())
					.put("nextClientNickname", startGrabClient.getNickname())
					.put("nextClientId", startGrabClient.getId())
					.put("pokers", client.getPokers())
					.json();

			if(client.getRole() == ClientRole.PLAYER) {
				ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_GAME_STARTING, result);
			}else {
				if(startGrabClient.getId() == client.getId()) {
					//RobotEventListener.get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(client, result);
					RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY).call(client, null);
				}
			}

		}


	}

}
