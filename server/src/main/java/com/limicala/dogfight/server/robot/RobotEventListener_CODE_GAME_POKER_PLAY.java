package com.limicala.dogfight.server.robot;

import com.limicala.dogfight.robot.RobotDecisionMakers;
import com.limicala.dogfight.server.ServerContains;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.PokerSell;
import org.nico.noson.Noson;
import com.limicala.dogfight.entity.Room;
import com.limicala.dogfight.enums.SellType;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.server.event.ServerEventListener;

public class RobotEventListener_CODE_GAME_POKER_PLAY implements RobotEventListener{

	@Override
	public void call(ClientSide robot, String data) {
		ServerContains.THREAD_EXCUTER.execute(() -> {
			Room room = ServerContains.getRoom(robot.getRoomId());

			PokerSell lastPokerShell = null;
			PokerSell pokerSell = null;
			if(room.getLastSellClient() != robot.getId()) {
				lastPokerShell = room.getLastPokerShell();
				pokerSell = RobotDecisionMakers.howToPlayPokers(room.getDifficultyCoefficient(), lastPokerShell, robot, room.getClientSideList());
			}else {
				pokerSell = RobotDecisionMakers.howToPlayPokers(room.getDifficultyCoefficient(), null, robot, room.getClientSideList());
			}

			if(pokerSell == null || pokerSell.getSellType() == SellType.ILLEGAL) {
				ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_PASS).call(robot, data);
			}else {
				Character[] cs = new Character[pokerSell.getSellPokers().size()];
				for(int index = 0; index < cs.length; index ++) {
					cs[index] = pokerSell.getSellPokers().get(index).getLevel().getAlias()[0];
				}
				ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY).call(robot, Noson.reversal(cs));
			}
		});
	}
}
