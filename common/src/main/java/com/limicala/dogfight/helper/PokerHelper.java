package com.limicala.dogfight.helper;

import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.entity.Poker;
import com.limicala.dogfight.entity.PokerSell;
import com.limicala.dogfight.enums.PokerLevel;
import com.limicala.dogfight.enums.PokerType;
import com.limicala.dogfight.enums.SellType;
import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.utils.ListUtils;

import java.util.*;

public class PokerHelper {
	/**
	 * Print the type of poker style 
	 */
	public static int pokerPrinterType = 0;

	public static int totalPrinters = 5;
	
	/**
	 * The list of all pokers, by 54
	 */
	private static List<Poker> basePokers = new ArrayList<Poker>(54);

	private static Comparator<Poker> pokerComparator = new Comparator<Poker>() {
		@Override
		public int compare(Poker o1, Poker o2) {
		    if (o1.getLevel().getLevel() == o2.getLevel().getLevel()){
		        return o1.getType().compareTo(o2.getType());
            }
		    return o1.getLevel().getLevel() - o2.getLevel().getLevel();
		}
	};

	static {
		PokerLevel[] pokerLevels = PokerLevel.values();
		PokerType[] pokerTypes = PokerType.values();

		for(PokerLevel level: pokerLevels) {
			if(level == PokerLevel.LEVEL_BIG_KING) {
				basePokers.add(new Poker(level, PokerType.BLANK));
				continue;
			}
			if(level == PokerLevel.LEVEL_SMALL_KING) {
				basePokers.add(new Poker(level, PokerType.BLANK));
				continue;
			}
			for(PokerType type: pokerTypes) {
				if(type == PokerType.BLANK) {
					continue;
				}
				basePokers.add(new Poker(level, type));
			}
		}
	}

	public static void sortPoker(List<Poker> pokers){
		Collections.sort(pokers, pokerComparator);
	}

	public static int[] getIndexes(Character[] options, List<Poker> pokers) {
		List<Poker> copyList = new ArrayList<>(pokers.size());
		copyList.addAll(pokers);
		int[] indexes = new int[options.length];
		for(int index = 0; index < options.length; index ++) {
			char option = options[index];
			boolean isTarget = false;
			for(int pi = 0; pi < copyList.size(); pi ++) {
				Poker poker = copyList.get(pi);
				if(poker != null) {
					if(Arrays.asList(poker.getLevel().getAlias()).contains(option)) {
						isTarget = true;
						//Index start from 1, not 0
						indexes[index] = pi + 1;
						copyList.set(pi, null);
						break;
					}
				}
			}
			if(! isTarget) {
				return null;
			}
		}
		Arrays.sort(indexes);
		return indexes;
	}

	public static boolean checkPokerIndex(int[] indexes, List<Poker> pokers){
		boolean access = true;
		if(indexes == null || indexes.length == 0) {
			access = false;
		}else {
			for(int index: indexes){
				if(index > pokers.size() || index < 1){
					access = false;
				}
			}
		}
		return access;
	}

	public static PokerSell checkPokerType(List<Poker> pokers) {

		if(pokers != null && ! pokers.isEmpty()) {
			sortPoker(pokers);

			int[] levelTable = new int[20];
			for(Poker poker: pokers) {
				levelTable[poker.getLevel().getLevel()] ++; 
			}

			int startIndex = -1;
			int endIndex = -1;
			int count = 0;

			int singleCount = 0;
			int doubleCount = 0;
			int threeCount = 0;
			int threeStartIndex = -1;
			int threeEndIndex = -1;
			int fourCount = 0;
			int fourStartIndex = -1;
			int fourEndIndex = -1;
			for(int index = 0; index < levelTable.length; index ++) {
				int value = levelTable[index];
				if(value != 0) {
					endIndex = index;
					count ++;
					if(startIndex == -1) {
						startIndex = index;
					}
					if(value == 1) {
						singleCount ++;
					}else if(value == 2) {
						doubleCount ++;
					}else if(value == 3) {
						if(threeStartIndex == -1) {
							threeStartIndex = index;
						}
						threeEndIndex = index;
						threeCount ++;
					}else if(value == 4) {
						if(fourStartIndex == -1) {
							fourStartIndex = index;
						}
						fourEndIndex = index;
						fourCount ++;
					}
				}
			}

			if(singleCount == doubleCount && singleCount == threeCount && singleCount == 0 && fourCount == 1) {
				return new PokerSell(SellType.BOMB, pokers, startIndex);
			}

			if(singleCount == 2 && startIndex == PokerLevel.LEVEL_SMALL_KING.getLevel() && endIndex == PokerLevel.LEVEL_BIG_KING.getLevel()) {
				return new PokerSell(SellType.KING_BOMB, pokers, PokerLevel.LEVEL_SMALL_KING.getLevel());
			}

			if(startIndex == endIndex) {
				if(levelTable[startIndex] == 1) {
					return new PokerSell(SellType.SINGLE, pokers, startIndex);
				}else if(levelTable[startIndex] == 2) {
					return new PokerSell(SellType.DOUBLE, pokers, startIndex);
				}else if(levelTable[startIndex] == 3) {
					return new PokerSell(SellType.THREE, pokers, startIndex);
				}
			}
			if(endIndex - startIndex == count - 1 && endIndex < PokerLevel.LEVEL_2.getLevel()) {
				if(levelTable[startIndex] == 1 && singleCount > 4 && doubleCount + threeCount + fourCount == 0) {
					return new PokerSell(SellType.SINGLE_STRAIGHT, pokers, endIndex);
				}else if(levelTable[startIndex] == 2 && doubleCount > 2 && singleCount + threeCount + fourCount == 0) {
					return new PokerSell(SellType.DOUBLE_STRAIGHT, pokers, endIndex);
				}else if(levelTable[startIndex] == 3 && threeCount > 1 && doubleCount + singleCount + fourCount == 0) {
					return new PokerSell(SellType.THREE_STRAIGHT, pokers, endIndex);
				}else if(levelTable[startIndex] == 4 && fourCount > 1 && doubleCount + threeCount + singleCount == 0) {
					return new PokerSell(SellType.FOUR_STRAIGHT, pokers, endIndex);
				}
			}

			if(threeCount != 0) {
				if(singleCount != 0 && singleCount == threeCount && doubleCount == 0 && fourCount == 0) {
					if(threeCount == 1) {
						return new PokerSell(SellType.THREE_ZONES_SINGLE, pokers, threeEndIndex);
					}else {
						if(threeEndIndex - threeStartIndex + 1 == threeCount && threeEndIndex < PokerLevel.LEVEL_2.getLevel()) {
							return new PokerSell(SellType.THREE_STRAIGHT_WITH_SINGLE, pokers, threeEndIndex);
						}
					}
				}else if(doubleCount != 0 && doubleCount == threeCount && singleCount == 0 && fourCount == 0) {
					if(threeCount == 1) {
						return new PokerSell(SellType.THREE_ZONES_DOUBLE, pokers, threeEndIndex);
					}else {
						if(threeEndIndex - threeStartIndex + 1 == threeCount && threeEndIndex < PokerLevel.LEVEL_2.getLevel()) {
							return new PokerSell(SellType.FOUR_STRAIGHT_WITH_DOUBLE, pokers, threeEndIndex);
						}
					}
				}
			}

			if(fourCount != 0) {
				if(singleCount != 0 && singleCount == fourCount * 2 && doubleCount == 0 && threeCount == 0) {
					if(fourCount == 1) {
						return new PokerSell(SellType.FOUR_ZONES_SINGLE, pokers, fourEndIndex);
					}else {
						if(fourEndIndex - fourStartIndex + 1 == fourCount && fourEndIndex < PokerLevel.LEVEL_2.getLevel()) {
							return new PokerSell(SellType.FOUR_STRAIGHT_WITH_SINGLE, pokers, fourEndIndex);
						}
					}
				}else if(doubleCount != 0 && doubleCount == fourCount * 2 && singleCount == 0 && threeCount == 0) {
					if(fourCount == 1) {
						return new PokerSell(SellType.FOUR_ZONES_DOUBLE, pokers, fourEndIndex);
					}else {
						if(fourEndIndex - fourStartIndex + 1 == fourCount && fourEndIndex < PokerLevel.LEVEL_2.getLevel()) {
							return new PokerSell(SellType.FOUR_STRAIGHT_WITH_DOUBLE, pokers, fourEndIndex);
						}
					}
				}
			}
		}
		return new PokerSell(SellType.ILLEGAL, null, -1);
	}

	public static int parseScore(SellType sellType, int level) {
		if(sellType == SellType.BOMB) {
			return level * 4 + 999;
		}else if(sellType == SellType.KING_BOMB) {
			return Integer.MAX_VALUE;
		}else if(sellType == SellType.SINGLE || sellType == SellType.DOUBLE || sellType == SellType.THREE) {
			return level;
		}else if(sellType == SellType.SINGLE_STRAIGHT || sellType == SellType.DOUBLE_STRAIGHT || sellType == SellType.THREE_STRAIGHT || sellType == SellType.FOUR_STRAIGHT) {
			return level;
		}else if(sellType == SellType.THREE_ZONES_SINGLE || sellType == SellType.THREE_STRAIGHT_WITH_SINGLE || sellType == SellType.THREE_ZONES_DOUBLE || sellType == SellType.FOUR_STRAIGHT_WITH_DOUBLE) {
			return level;
		}else if(sellType == SellType.FOUR_ZONES_SINGLE || sellType == SellType.FOUR_STRAIGHT_WITH_SINGLE || sellType == SellType.FOUR_ZONES_DOUBLE || sellType == SellType.FOUR_STRAIGHT_WITH_DOUBLE) {
			return level;
		}
		return -1;
	}

	public static List<Poker> getPoker(int[] indexes, List<Poker> pokers){
		List<Poker> resultPokers = new ArrayList<>(indexes.length);
		for(int index: indexes){
			resultPokers.add(pokers.get(index - 1));
		}
		sortPoker(resultPokers);
		return resultPokers;
	}

	public static boolean comparePoker(List<Poker> pres, List<Poker> currents){

		return true;
	}

	public static List<List<Poker>> distributePoker(){
		Collections.shuffle(basePokers);
		List<List<Poker>> pokersList = new ArrayList<List<Poker>>();
		List<Poker> pokers1 = new ArrayList<>(18);
		pokers1.addAll(basePokers.subList(0, 18));
		List<Poker> pokers2 = new ArrayList<>(18);
		pokers2.addAll(basePokers.subList(18, 36));
		List<Poker> pokers3 = new ArrayList<>(18);
		pokers3.addAll(basePokers.subList(36, 54));
		pokersList.add(pokers1);
		pokersList.add(pokers2);
		pokersList.add(pokers3);
		for(List<Poker> pokers: pokersList) {
			sortPoker(pokers);
		}
		return pokersList;
	}

	public static String printPoker(List<Poker> pokers) {
		sortPoker(pokers);
		switch(pokerPrinterType){
		case 0:
			return buildHandStringSharp(pokers);
		case 1:
			return buildHandStringRounded(pokers);
		case 2:
			return textOnly(pokers);
		case 3:
			return textOnlyNoType(pokers);
		default:
			return buildHandStringSharp(pokers);

		}

	}
	private static String buildHandStringSharp(List<Poker> pokers){
		StringBuilder builder = new StringBuilder();
		if(pokers != null && pokers.size() > 0) {

			for(int index = 0; index < pokers.size(); index ++) {
				if(index == 0) {
					builder.append("┌──┐");
				}else {
					builder.append("──┐");
				}
			}
			builder.append(System.lineSeparator());
			for(int index = 0; index < pokers.size(); index ++) {
				if(index == 0) {
					builder.append("│");
				}
				String name = pokers.get(index).getLevel().getName();
				builder.append(name + (name.length() == 1 ? " " : "" ) +  "|");
			}
			builder.append(System.lineSeparator());
			for(int index = 0; index < pokers.size(); index ++) {
				if(index == 0) {
					builder.append("│");
				}
				builder.append(pokers.get(index).getType().getName() + " |");
			}
			builder.append(System.lineSeparator());
			for(int index = 0; index < pokers.size(); index ++) {
				if(index == 0) {
					builder.append("└──┘");
				} else {
					builder.append("──┘");
				}
			}
		}
		return builder.toString();
	}
	private static String buildHandStringRounded(List<Poker> pokers){
		StringBuilder builder = new StringBuilder();
		if(pokers != null && pokers.size() > 0) {

			for(int index = 0; index < pokers.size(); index ++) {
				if(index == 0) {
					builder.append("┌──╮");
				}else {
					builder.append("──╮");
				}
			}
			builder.append(System.lineSeparator());
			for(int index = 0; index < pokers.size(); index ++) {
				if(index == 0) {
					builder.append("│");
				}
				String name = pokers.get(index).getLevel().getName();
				builder.append(name + (name.length() == 1 ? " " : "" ) +  "|");
			}
			builder.append(System.lineSeparator());
			for(int index = 0; index < pokers.size(); index ++) {
				if(index == 0) {
					builder.append("│");
				}
				builder.append(pokers.get(index).getType().getName() + " |");
			}
			builder.append(System.lineSeparator());
			for(int index = 0; index < pokers.size(); index ++) {
				if(index == 0) {
					builder.append("└──╯");
				} else {
					builder.append("──╯");
				}
			}
		}
		return builder.toString();
	}

	private static String textOnly(List<Poker> pokers){
		StringBuilder builder = new StringBuilder();
		if(pokers != null && pokers.size() > 0) {
			for(int index = 0; index < pokers.size(); index ++) {
				String name = pokers.get(index).getLevel().getName();
				String type = pokers.get(index).getType().getName();

				builder.append(name + type);
			}
		}
		return builder.toString();
	}
	private static String textOnlyNoType(List<Poker> pokers){
		StringBuilder builder = new StringBuilder();
		if(pokers != null && pokers.size() > 0) {
			for(int index = 0; index < pokers.size(); index ++) {
				String name = pokers.get(index).getLevel().getName();
				builder.append(name + " ");
			}
		}
		return builder.toString();
	}

	public static int parsePokerColligationScore(List<Poker> pokers){
		int score = 0;
		int count = 0;
		int increase = 0;
		int lastLevel = -1;
		if(pokers != null && ! pokers.isEmpty()){
			for(int index = 0; index < pokers.size(); index ++){
				int level = pokers.get(index).getLevel().getLevel();
				if(lastLevel == -1){
					increase ++;
					count ++;
					score += lastLevel;
				}else{
					if(level == lastLevel){
						++ count;
					}else{
						count = 1;
					}
					if(level < PokerLevel.LEVEL_2.getLevel() && level - 1 == lastLevel){
						++ increase;
					}else{
						increase = 1;
					}

					score += (count + (increase > 4 ? increase : 0)) * level;
				}

				if(level == PokerLevel.LEVEL_2.getLevel()){
					score += level * 2;
				}else if(level > PokerLevel.LEVEL_2.getLevel()){
					score += level * 3;
				}
				lastLevel = level;
			}
		}
		return score;
	}

	public static List<PokerSell> parsePokerSells(List<Poker> pokers){
		List<PokerSell> pokerSells = new ArrayList<>();
		int size = pokers.size();

		//all single or double
		{
			int count = 0;
			int lastLevel = -1;
			List<Poker> sellPokers = new ArrayList<>(4);
			for(int index = 0; index < pokers.size(); index ++) {
				Poker poker = pokers.get(index);
				int level = poker.getLevel().getLevel();
				if(lastLevel == -1) {
					++ count;
				}else {
					if(level == lastLevel) {
						++ count;
					}else {
						count = 1;
						sellPokers.clear();
					}
				}
				sellPokers.add(poker);
				if(count == 1) {
					pokerSells.add(new PokerSell(SellType.SINGLE, ListUtils.getList(sellPokers), poker.getLevel().getLevel()));
				}else if(count == 2){
					pokerSells.add(new PokerSell(SellType.DOUBLE, ListUtils.getList(sellPokers), poker.getLevel().getLevel()));
				}else if(count == 3){
					pokerSells.add(new PokerSell(SellType.THREE, ListUtils.getList(sellPokers), poker.getLevel().getLevel()));
				}else if(count == 4){
					pokerSells.add(new PokerSell(SellType.BOMB, ListUtils.getList(sellPokers), poker.getLevel().getLevel()));
				}

				lastLevel = level;
			}
		}
		//Shunzi
		{
			parsePokerSellStraight(pokerSells, SellType.SINGLE);
			parsePokerSellStraight(pokerSells, SellType.DOUBLE);
			parsePokerSellStraight(pokerSells, SellType.THREE);
			parsePokerSellStraight(pokerSells, SellType.BOMB);
		}
		
		//Shunzi with args
		{
			for(int index = 0; index < pokerSells.size(); index ++) {
				PokerSell sell = pokerSells.get(index);
				if(sell.getSellType() == SellType.THREE) {
					parseArgs(pokerSells, sell, 1, SellType.SINGLE, SellType.THREE_ZONES_SINGLE);
					parseArgs(pokerSells, sell, 1, SellType.DOUBLE, SellType.THREE_ZONES_DOUBLE);
				}else if(sell.getSellType() == SellType.BOMB) {
					parseArgs(pokerSells, sell, 2, SellType.SINGLE, SellType.FOUR_ZONES_SINGLE);
					parseArgs(pokerSells, sell, 2, SellType.DOUBLE, SellType.FOUR_ZONES_DOUBLE);
				}else if(sell.getSellType() == SellType.THREE_STRAIGHT) {
					int count = sell.getSellPokers().size() / 3;
					parseArgs(pokerSells, sell, count, SellType.SINGLE, SellType.THREE_STRAIGHT_WITH_SINGLE);
					parseArgs(pokerSells, sell, count, SellType.DOUBLE, SellType.THREE_STRAIGHT_WITH_DOUBLE);
				}else if(sell.getSellType() == SellType.FOUR_STRAIGHT) {
					int count = (sell.getSellPokers().size() / 4) * 2;
					parseArgs(pokerSells, sell, count, SellType.SINGLE, SellType.FOUR_STRAIGHT_WITH_SINGLE);
					parseArgs(pokerSells, sell, count, SellType.DOUBLE, SellType.FOUR_STRAIGHT_WITH_DOUBLE);
				}
			}
		}
		
		//king boom
		{
			if(size > 1) {
				if(pokers.get(size - 1).getLevel() == PokerLevel.LEVEL_BIG_KING && pokers.get(size - 2).getLevel() == PokerLevel.LEVEL_SMALL_KING) {
					pokerSells.add(new PokerSell(SellType.KING_BOMB, ListUtils.getList(new Poker[] {pokers.get(size - 2), pokers.get(size - 1)}), PokerLevel.LEVEL_BIG_KING.LEVEL_10.getLevel()));
				}
			}
		}
		
		return pokerSells;
	}
	
	private static void parseArgs(List<PokerSell> pokerSells, PokerSell pokerSell, int deep, SellType sellType, SellType targetSellType) {
		Set<Integer> existLevelSet = new HashSet<>();
		for(Poker p: pokerSell.getSellPokers()) {
			existLevelSet.add(p.getLevel().getLevel());
		}
		parseArgs(existLevelSet, pokerSells, new HashSet<>(), pokerSell, deep, sellType, targetSellType);
	}
	
	private static void parseArgs(Set<Integer> existLevelSet, List<PokerSell> pokerSells, Set<List<Poker>> pokersList, PokerSell pokerSell, int deep, SellType sellType, SellType targetSellType) {
		if(deep == 0) {
			List<Poker> allPokers = new ArrayList<>();
			allPokers.addAll(pokerSell.getSellPokers());
			for(List<Poker> ps: pokersList) {
				allPokers.addAll(ps);
			}
			pokerSells.add(new PokerSell(targetSellType, allPokers, pokerSell.getCoreLevel()));
			return;
		}
		
		for(int index = 0; index < pokerSells.size(); index ++) {
			PokerSell subSell = pokerSells.get(index);
			if(subSell.getSellType() == sellType && ! existLevelSet.contains(subSell.getCoreLevel())) {
				pokersList.add(subSell.getSellPokers());
				existLevelSet.add(subSell.getCoreLevel());
				parseArgs(existLevelSet, pokerSells, pokersList, pokerSell, deep - 1, sellType, targetSellType);
				existLevelSet.remove(subSell.getCoreLevel());
				pokersList.remove(subSell.getSellPokers());
			}
		}
	}

	private static void parsePokerSellStraight(List<PokerSell> pokerSells, SellType sellType) {
		int minLenght = -1;
		int width = -1;
		SellType targetSellType = null;
		if(sellType == SellType.SINGLE) {
			minLenght = 5;
			width = 1;
			targetSellType = SellType.SINGLE_STRAIGHT;
		}else if(sellType == SellType.DOUBLE) {
			minLenght = 3;
			width = 2;
			targetSellType = SellType.DOUBLE_STRAIGHT;
		}else if(sellType == SellType.THREE) {
			minLenght = 2;
			width = 3;
			targetSellType = SellType.THREE_STRAIGHT;
		}else if(sellType == SellType.BOMB) {
			minLenght = 2;
			width = 4;
			targetSellType = SellType.FOUR_STRAIGHT;
		}

		int increase_1 = 0;
		int lastLevel_1 = -1;
		List<Poker> sellPokers_1 = new ArrayList<>(4);
		for(int index = 0; index < pokerSells.size(); index ++) {
			PokerSell sell = pokerSells.get(index);

			if(sell.getSellType() == sellType) {
				int level = sell.getCoreLevel();
				if(lastLevel_1 == -1) {
					++ increase_1;
					sellPokers_1.addAll(sell.getSellPokers());
				}else {
					if(level - 1 == lastLevel_1 && level != PokerLevel.LEVEL_2.getLevel()) {
						++ increase_1;
						sellPokers_1.addAll(sell.getSellPokers());
					}else{
						if(increase_1 >= minLenght) {
							for(int s = 0; s <= increase_1 - minLenght; s ++) {
								int len = minLenght + s;
								for(int subIndex = 0; subIndex <= increase_1 - len; subIndex ++) {
									List<Poker> pokers = ListUtils.getList(sellPokers_1.subList(subIndex * width, (subIndex + len) * width));
									pokerSells.add(new PokerSell(targetSellType, pokers, pokers.get(pokers.size() - 1).getLevel().getLevel()));
								}
							}
						}
						increase_1 = 0;
						sellPokers_1.clear();
					}
				}
				lastLevel_1 = level;
			}
		}
		if(sellPokers_1 != null) {
			if(increase_1 >= minLenght) {
				for(int s = 0; s <= increase_1 - minLenght; s ++) {
					int len = minLenght + s;
					for(int subIndex = 0; subIndex <= increase_1 - len; subIndex ++) {
						List<Poker> pokers = ListUtils.getList(sellPokers_1.subList(subIndex * width, (subIndex + len) * width));
						pokerSells.add(new PokerSell(targetSellType, pokers, pokers.get(pokers.size() - 1).getLevel().getLevel()));
					}
				}
			}
			increase_1 = 0;
			sellPokers_1.clear();
		}
	}

	public static boolean isMin(PokerSell curr, ClientSide clientSide, List<ClientSide> sides) {
		SimplePrinter.serverLog("currPokerSell: " + curr);
		SimplePrinter.serverLog("clientSide: " + clientSide);
		sides.forEach(side -> SimplePrinter.serverLog("--side: " + side));

		boolean rv = false;
		int clientMinLevel = PokerLevel.LEVEL_BIG_KING.getLevel();
		int roomMinLevel = PokerLevel.LEVEL_BIG_KING.getLevel();
		int currentPokerMinLevel = PokerHelper.getPokersMinLevel(curr.getSellPokers());
		for (ClientSide side : sides) {
			int level = getPokersMinLevel(side.getPokers());
			if (side == clientSide) {
				clientMinLevel = level;
			}
			roomMinLevel = Math.min(roomMinLevel, level);
		}

		SimplePrinter.serverLog(String.format("roomMinLevel[%d] clientMinLevel[%d] currentPokerMinLevel[%d]",
				roomMinLevel, clientMinLevel, currentPokerMinLevel));

		if (roomMinLevel == clientMinLevel && currentPokerMinLevel > roomMinLevel) {
			rv = false;
		} else if (clientMinLevel == currentPokerMinLevel){
			rv = true;
		} else {
			List<PokerSell> pokerSells = parsePokerSells(clientSide.getPokers());
			for (PokerSell pokerSell : pokerSells) {
				if (pokerSell.getSellType().equals(curr.getSellType()) && pokerSell.getSellPokers().size() == curr.getSellPokers().size()) {
					if (pokerSell.getScore() == curr.getScore()) {
						rv = true;
					}
					break;
				}
			}
		}
		return rv;
	}

	public static int getPokersMinLevel(List<Poker> pokers){
		return pokers.get(0).getLevel().getLevel();
	}

	public static int getMinPokerShell(ClientSide clientSide, PokerSell sell){
		List<Poker> pokers = clientSide.getPokers();
//		List<Poker> dest = new LinkedList<>();
		int coreLevel = 3;

		int[] levelTable = new int[20];
		for (Poker poker : pokers){
			levelTable[poker.getLevel().getLevel()] ++;
		}
		switch (sell.getSellType()) {
			case SINGLE:
				coreLevel = pokers.get(0).getLevel().getLevel();
				break;
			case DOUBLE:
				for (int i = 3; i < levelTable.length; ++i){
					if (levelTable[i] >= 2){
						coreLevel = i;
						break;
					}
				}
				break;
			case THREE:
			case THREE_ZONES_SINGLE:
			case THREE_ZONES_DOUBLE:
				for (int i = 3; i < levelTable.length; ++i){
					if (levelTable[i] >= 3){
						coreLevel = i;
						break;
					}
				}
				break;
			case KING_BOMB:
			case BOMB:
			case FOUR_ZONES_SINGLE:
			case FOUR_ZONES_DOUBLE:
				for (int i = 3; i < levelTable.length; ++i){
					if (levelTable[i] >= 4){
						coreLevel = i;
						break;
					}
				}
				break;
			case SINGLE_STRAIGHT:

		}
		return coreLevel;
	}

	public static int getMinPokerShell(List<Poker> pokers, PokerSell pokerSell) {
		List<PokerSell> pokerSells = new ArrayList<>();
		int size = pokers.size();

		//all single or double
		{
			int count = 0;
			int lastLevel = -1;
			List<Poker> sellPokers = new ArrayList<>(4);
			for (int index = 0; index < pokers.size(); index++) {
				Poker poker = pokers.get(index);
				int level = poker.getLevel().getLevel();
				if (lastLevel == -1) {
					++count;
				} else {
					if (level == lastLevel) {
						++count;
					} else {
						count = 1;
						sellPokers.clear();
					}
				}
				sellPokers.add(poker);
				PokerSell temp = null;
				if (count == 1) {
					temp = new PokerSell(SellType.SINGLE, ListUtils.getList(sellPokers), poker.getLevel().getLevel());
				} else if (count == 2) {
					temp = new PokerSell(SellType.DOUBLE, ListUtils.getList(sellPokers), poker.getLevel().getLevel());
				} else if (count == 3) {
					temp = new PokerSell(SellType.THREE, ListUtils.getList(sellPokers), poker.getLevel().getLevel());
				} else if (count == 4) {
					temp = new PokerSell(SellType.BOMB, ListUtils.getList(sellPokers), poker.getLevel().getLevel());
				}


				if (temp.getSellType() == pokerSell.getSellType() || (SellType.BOMB == temp.getSellType() && SellType.KING_BOMB == pokerSell.getSellType())) {
					return temp.getCoreLevel();
				}
				pokerSells.add(temp);
				lastLevel = level;
			}
		}

		int len = pokerSells.size();

		int pokerSellLength = pokerSell.getSellPokers().size();
		if (SellType.SINGLE_STRAIGHT == pokerSell.getSellType()) {
			parsePokerSellStraight(pokerSells, SellType.SINGLE);
		}

		if (SellType.DOUBLE_STRAIGHT == pokerSell.getSellType()) {
			parsePokerSellStraight(pokerSells, SellType.DOUBLE);
		}

		if (SellType.THREE_STRAIGHT == pokerSell.getSellType()
				|| SellType.THREE_STRAIGHT_WITH_SINGLE == pokerSell.getSellType()
				|| SellType.THREE_STRAIGHT_WITH_DOUBLE == pokerSell.getSellType()) {
			if (SellType.THREE_STRAIGHT_WITH_SINGLE == pokerSell.getSellType()){
				pokerSellLength = (pokerSellLength / 4) * 3;
			}
			if (SellType.THREE_STRAIGHT_WITH_DOUBLE == pokerSell.getSellType()){
				pokerSellLength = (pokerSellLength / 5) * 3;
			}
			parsePokerSellStraight(pokerSells, SellType.THREE);
		}

		for (int i = len; i < pokerSells.size(); ++i){
			System.out.println(pokerSells.get(i));
		}

		for (int i = len; i < pokerSells.size(); ++i){
			PokerSell t = pokerSells.get(i);
			if (t.getSellPokers().size() == pokerSellLength){
				return t.getCoreLevel();
			}
		}

		return 3;
	}
}
