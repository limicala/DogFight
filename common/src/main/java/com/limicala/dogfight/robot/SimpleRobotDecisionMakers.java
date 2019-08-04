package com.limicala.dogfight.robot;

import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Poker;
import com.limicala.dogfight.entity.PokerSell;
import com.limicala.dogfight.enums.SellType;
import com.limicala.dogfight.helper.PokerHelper;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/** 
 * 
 * @author nico
 * @version createTime：2018年11月15日 上午12:13:49
 */

public class SimpleRobotDecisionMakers extends AbstractRobotDecisionMakers{
	@Override
	public PokerSell howToPlayPokers(PokerSell lastPokerSell, ClientSide clientSide, List<ClientSide> sides) {
		if(lastPokerSell != null && lastPokerSell.getSellType() == SellType.KING_BOMB) {
			return null;
		}

		List<PokerSell> sells = PokerHelper.parsePokerSells(clientSide.getPokers());
		if(lastPokerSell == null) {
			Collections.shuffle(sells);
			for (PokerSell sell : sells){
				if (PokerHelper.isMin(sell, clientSide, sides)){
					return sell;
				}
			}
		}

		for(PokerSell sell: sells) {
			if(sell.getSellType() == lastPokerSell.getSellType()) {
				if(sell.getScore() > lastPokerSell.getScore()) {
					return sell;
				}
			}
		}
		if(lastPokerSell.getSellType() != SellType.BOMB) {
			for(PokerSell sell: sells) {
				if(sell.getSellType() == SellType.BOMB) {
					return sell;
				}
			}
		}

		return null;
	}
}
