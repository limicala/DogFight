package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.print.FormatPrinter;
import com.limicala.dogfight.print.SimplePrinter;

import java.util.List;
import java.util.Map;

import static com.limicala.dogfight.client.event.ClientEventListener_CODE_CLIENT_NICKNAME_SET.NICKNAME_MAX_LENGTH;

public class ClientEventListener_CODE_SHOW_ROOMS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		
		List<Map<String, Object>> roomList = Noson.convert(data, new NoType<List<Map<String, Object>>>() {});
		if(roomList != null && ! roomList.isEmpty()){
			// "COUNT" begins after NICKNAME_MAX_LENGTH characters. The dash means that the string is left-justified.
			String format = "#\t%s\t|\t%-" + NICKNAME_MAX_LENGTH + "s\t|\t%s\t#\n";
			FormatPrinter.printNotice(format, "ID", "OWNER", "COUNT");
			for(Map<String, Object> room: roomList) {
				FormatPrinter.printNotice(format, room.get("roomId"), room.get("roomOwner"), room.get("roomClientCount"));
			}
			SimplePrinter.printNotice("");
			get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
		}else {
			SimplePrinter.printNotice("No available room, please create a room ！");
			get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
		}
	}



}
