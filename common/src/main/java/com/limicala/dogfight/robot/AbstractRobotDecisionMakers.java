package com.limicala.dogfight.robot;

import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Poker;
import com.limicala.dogfight.entity.PokerSell;

import java.util.List;

/** 
 * 
 * @author nico
 * @version createTime：2018年11月15日 上午12:12:15
 */

public abstract	class AbstractRobotDecisionMakers {

	public abstract PokerSell howToPlayPokers(PokerSell lastPokerSell, ClientSide clientSide, List<ClientSide> sides);
}
