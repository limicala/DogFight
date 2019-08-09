package com.limicala.dogfight.print;

import com.limicala.dogfight.entity.Poker;
import com.limicala.dogfight.helper.PokerHelper;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplePrinter {

	private final static SimpleDateFormat FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	public static int pokerDisplayFormat = 0;
	
	public static void printPokers(List<Poker> pokers) {
		System.out.println(PokerHelper.printPoker(pokers));
	}
	
	public static void printNotice(String msg) {
		try {
			System.out.println(new String(msg.getBytes("ISO-8859-1"), "UTF-8"));
		}catch (UnsupportedEncodingException ignore){

		}
	}
	
	public static void printNotice(String msgKey, String locale) {
		//TODO : read locale
		Map<String,Map<String, String>> map = new HashMap<String,Map<String, String>>();
		map.put("english", new HashMap<String, String>());
		map.get("eng").put("caterpillar", "caterpillar's message!!");

		System.out.println(map.get(locale).get(msgKey));
	}

	public static void serverLog(String msg) {
		System.out.println(FORMAT.format(new Date()) + "-> " + msg);
	}
}
