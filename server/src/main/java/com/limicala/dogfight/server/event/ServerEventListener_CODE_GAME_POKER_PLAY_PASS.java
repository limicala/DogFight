package com.limicala.dogfight.server.event;

import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.PokerSell;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.ClientRole;
import com.limicala.dogfight.enums.SellType;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.helper.PokerHelper;
import com.limicala.dogfight.server.ServerContains;
import com.limicala.dogfight.server.robot.RobotEventListener;

import java.util.List;

public class ServerEventListener_CODE_GAME_POKER_PLAY_PASS implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {
		Room room = ServerContains.getRoom(clientSide.getRoomId());

		if(room != null) {
			if(room.getCurrentSellClient() == clientSide.getId()) {
				if(clientSide.getId() != room.getLastSellClient()) {
					PokerSell lastPokerSell = room.getLastPokerShell();
					List<PokerSell> sells = PokerHelper.parsePokerSells(clientSide.getPokers());

					boolean hasBigger = false;
					for(PokerSell sell: sells) {
						if(SellType.KING_BOMB == sell.getSellType() || sell.getSellType() == lastPokerSell.getSellType()) {
							if(sell.getScore() > lastPokerSell.getScore()) {
								hasBigger = true;
								break;
							}
						}
					}
					if (!hasBigger) {
						if (lastPokerSell.getSellType() != SellType.BOMB) {
							for (PokerSell sell : sells) {
								if (sell.getSellType() == SellType.BOMB) {
									hasBigger = true;
									break;
								}
							}
						}
					}

					if (hasBigger){
						ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_HAS_BIGGER, null);
						return;
					}

					ClientSide turnClient = clientSide.getNext();

					room.setCurrentSellClient(turnClient.getId());

					for(ClientSide client: room.getClientSideList()) {
						String result = MapHelper.newInstance()
								.put("clientId", clientSide.getId())
								.put("clientNickname", clientSide.getNickname())
								.put("nextClientId", turnClient.getId())
								.put("nextClientNickname", turnClient.getNickname())
								.json();
						if(client.getRole() == ClientRole.PLAYER) {
							ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_PASS, result);
						}else {
							if(client.getId() == turnClient.getId()) {
								RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY).call(turnClient, data);
							}
						}
					}
				}else {
					ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_CANT_PASS, null);
				}
			}else {
				ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_ORDER_ERROR, null);
			}
		}else {
			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_PLAY_FAIL_BY_INEXIST, null);
		}
	}

}
