package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.print.SimpleWriter;
import com.limicala.dogfight.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS_PVP extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice(ConsoleMessage.PVP);
		String line = SimpleWriter.write("pvp");
		
		if(line.equalsIgnoreCase("BACK")) {
			get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
		}else {
			while(line == null || OptionsUtils.getOptions(line) == -1) {
				SimplePrinter.printNotice(ConsoleMessage.INVALID_OPTION);
				line = SimpleWriter.write("pvp");
			}
			
			int choose = Integer.valueOf(line);
			
			if(choose == 1) {
				pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE, null);
			}else if(choose == 2){
				pushToServer(channel, ServerEventCode.CODE_GET_ROOMS, null);
			}else if(choose == 3){
				SimplePrinter.printNotice(ConsoleMessage.ENTER_ROOMID);
				line = SimpleWriter.write("roomid");
				
				if(line.equalsIgnoreCase("BACK")) {
					call(channel, data);
				}else {
					int option = OptionsUtils.getOptions(line);
					if(line == null || option < 1) {
						SimplePrinter.printNotice(ConsoleMessage.INVALID_OPTION);
						call(channel, data);
					}else{
						pushToServer(channel, ServerEventCode.CODE_ROOM_JOIN, String.valueOf(option));
					}
				}
			}else {
				SimplePrinter.printNotice(ConsoleMessage.INVALID_OPTION);
				call(channel, data);
			}
		}
		
	}



}
