package com.limicala.dogfight.server.timer;

import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.server.ServerContains;
import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.server.robot.RobotEventListener;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.*;
import com.limicala.dogfight.server.event.ServerEventListener;

import java.util.Map;
import java.util.TimerTask;

/** 
 * 
 * @author nico
 */

public class RoomClearTask extends TimerTask{

	//The room wait time of after create is 100s
	private static long waitingStatusInterval = 1000 * 100;

	//The room starting destroy time is 100s
	private static long startingStatusInterval = 1000 * 100;

	@Override
	public void run() {

		Map<Integer, Room> rooms = ServerContains.getRoomMap();
		if(rooms != null && ! rooms.isEmpty()){
			long now = System.currentTimeMillis();
			for(Room room: rooms.values()){
				long interval = 0;
				if(room.getStatus() != RoomStatus.STARTING){
					interval = waitingStatusInterval;
				}else{
					interval = startingStatusInterval;
				}
				long diff = now - room.getLastFlushTime();
				System.out.println(room.getId() + "->" + diff);
				if(diff > interval){
					boolean allRobots = true;
					for(ClientSide client: room.getClientSideList()) {
						if(client.getId() != room.getCurrentSellClient() && client.getRole() == ClientRole.PLAYER) {
							allRobots = false;
							break;
						}
					}
					
					ClientSide currentPlayer = room.getClientSideMap().get(room.getCurrentSellClient());
					
					if(allRobots) {
						ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(currentPlayer, null);
					}else {
						//kick this client
						ChannelUtils.pushToClient(currentPlayer.getChannel(), ClientEventCode.CODE_CLIENT_KICK, null);

						//client current player
						room.getClientSideMap().remove(currentPlayer.getId());
						room.getClientSideList().remove(currentPlayer);

						ClientSide robot = new ClientSide(- ServerContains.getClientId(), ClientStatus.PLAYING, null);
						robot.setNickname(currentPlayer.getNickname());
						robot.setRole(ClientRole.ROBOT);
						robot.setRoomId(room.getId());
						robot.setNext(currentPlayer.getNext());
						robot.setPre(currentPlayer.getPre());
						robot.getNext().setPre(robot);
						robot.getPre().setNext(robot);
						robot.setPokers(currentPlayer.getPokers());
						robot.setType(currentPlayer.getType());

						room.getClientSideMap().put(robot.getId(), robot);
						room.getClientSideList().add(robot);
						room.setCurrentSellClient(currentPlayer.getId());

						//If last sell client is current client, replace it to robot id
						if(room.getLastSellClient() == currentPlayer.getId()) {
							room.setLastSellClient(robot.getId());
						}

						//set robot difficulty -> simple
						room.setDifficultyCoefficient(1);

						ServerContains.CLIENT_SIDE_MAP.put(robot.getId(), robot);
						
						//init client
						currentPlayer.init();


						SimplePrinter.serverLog("room " + room.getId() + " player " + currentPlayer.getNickname() + " " + startingStatusInterval + "ms not operating, automatic custody!");

						RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY).call(robot, null);
					}
				}
			}
		}
	}

}
