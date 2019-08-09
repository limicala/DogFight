/* https://github.com/ainilili/ratel/tree/dev-1.1.0
   Copyright ainilili

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.limicala.dogfight.server.event;

import org.nico.noson.Noson;
import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Poker;
import com.limicala.dogfight.entity.PokerSell;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.*;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.helper.PokerHelper;
import com.limicala.dogfight.helper.TimeHelper;
import com.limicala.dogfight.server.ServerContains;
import com.limicala.dogfight.server.robot.RobotEventListener;

import java.util.List;

public class ServerEventListener_CODE_GAME_POKER_PLAY implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {
		Room room = ServerContains.getRoom(clientSide.getRoomId());
		if(room != null) {
			if(room.getCurrentSellClient() == clientSide.getId()) {
				Character[] options = Noson.convert(data, Character[].class);

				int[] indexes = PokerHelper.getIndexes(options, clientSide.getPokers());
				if(PokerHelper.checkPokerIndex(indexes, clientSide.getPokers())){
					boolean sellFlag = true;
					
					List<Poker> currentPokers = PokerHelper.getPoker(indexes, clientSide.getPokers());
					PokerSell currentPokerShell = PokerHelper.checkPokerType(currentPokers);
					if(currentPokerShell.getSellType() != SellType.ILLEGAL) {
						if(room.getLastSellClient() != clientSide.getId() && room.getLastPokerShell() != null){
							PokerSell lastPokerShell = room.getLastPokerShell();
							
							if((lastPokerShell.getSellType() != currentPokerShell.getSellType() || lastPokerShell.getSellPokers().size() != currentPokerShell.getSellPokers().size()) && currentPokerShell.getSellType() != SellType.BOMB && currentPokerShell.getSellType() != SellType.KING_BOMB) {
								String result = MapHelper.newInstance()
													.put("playType", currentPokerShell.getSellType())
													.put("playCount", currentPokerShell.getSellPokers().size())
													.put("preType", lastPokerShell.getSellType())
													.put("preCount", lastPokerShell.getSellPokers().size())
													.json();
								sellFlag = false;
								ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_MISMATCH, result);
							}else if(lastPokerShell.getScore() >= currentPokerShell.getScore()) {
								String result = MapHelper.newInstance()
										.put("playScore", currentPokerShell.getScore())
										.put("preScore", lastPokerShell.getScore())
										.json();
								sellFlag = false;
								ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_LESS, result);
							}
						}else {
							if (!PokerHelper.isMin(currentPokerShell, clientSide, room.getClientSideList())){
								String result = MapHelper.newInstance()
										.put("playType", currentPokerShell.getSellType())
										.put("playCount", currentPokerShell.getSellPokers().size())
										.put("preType", currentPokerShell.getSellType()) //TODO
										.put("preCount", currentPokerShell.getSellPokers().size()) //TODO
										.json();
								sellFlag = false;
								ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_MISMATCH_MIN, result);
							}
						}
					}else {
						sellFlag = false;
						ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_INVALID, null);
					}
					
					if(sellFlag) {
						ClientSide next = clientSide.getNext();
						
						room.setLastSellClient(clientSide.getId());
						room.setLastPokerShell(currentPokerShell);
						room.setCurrentSellClient(next.getId());
						
						clientSide.getPokers().removeAll(currentPokers);
						String result = MapHelper.newInstance()
								.put("clientId", clientSide.getId())
								.put("clientNickname", clientSide.getNickname())
								.put("pokers", currentPokers)
								.put("sellClientNickname", next.getNickname())
								.json();
						for(ClientSide client: room.getClientSideList()) {
							if(client.getRole() == ClientRole.PLAYER) {
								ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_SHOW_POKERS, result);
							}
						}
						
						TimeHelper.sleep(500);
						
						if(clientSide.getPokers().isEmpty()) {
							result = MapHelper.newInstance()
												.put("winnerNickname", clientSide.getNickname())
												.json();
							
							for(ClientSide client: room.getClientSideList()) {
								if(client.getRole() == ClientRole.PLAYER) {
									ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_GAME_OVER, result);
								}
								ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(client, data);
							}
						}else {
							if(next.getRole() == ClientRole.PLAYER) {
								ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(next, result);
							}else {
								RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY).call(next, data);
							}
						}
					}
				}else{
					ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_INVALID, null);
				}
			}else {
				ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_ORDER_ERROR, null);
			}
		}else {
			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_PLAY_FAIL_BY_INEXIST, null);
		}
	}

}
