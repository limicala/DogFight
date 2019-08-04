package com.limicala.dogfight.robot;

import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.PokerSell;
import com.limicala.dogfight.entity.Poker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * How does the machine decide on a better strategy to win the game
 * 
 * @author nico
 */
public class RobotDecisionMakers {
	
	private static Map<Integer, AbstractRobotDecisionMakers> decisionMakersMap = new HashMap<Integer, AbstractRobotDecisionMakers>() {
		private static final long serialVersionUID = 8541568961784067309L;
		{
			put(1, new SimpleRobotDecisionMakers());
		}
	};
	
	public static boolean contains(int difficultyCoefficient) {
		return decisionMakersMap.containsKey(difficultyCoefficient);
	}
	
	public static PokerSell howToPlayPokers(int difficultyCoefficient, PokerSell lastPokerSell, ClientSide clientSide, List<ClientSide> sides){
		return decisionMakersMap.get(difficultyCoefficient).howToPlayPokers(lastPokerSell, clientSide, sides);
	}
}
