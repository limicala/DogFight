package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.print.SimpleWriter;
import com.limicala.dogfight.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS_PVE extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice(ConsoleMessage.PVE);
		String line = SimpleWriter.write("pve");
		
		if(line.equalsIgnoreCase("BACK")) {
			get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
		}else {

			while(line == null || OptionsUtils.getOptions(line) == -1) {
				SimplePrinter.printNotice(ConsoleMessage.INVALID_OPTION);
				line = SimpleWriter.write("pve");
			}
			
			int choose = Integer.valueOf(line);
			
			if(0 < choose && choose < 4) {
				pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE_PVE, String.valueOf(choose));
			}else {
				SimplePrinter.printNotice(ConsoleMessage.INVALID_OPTION);
				call(channel, data);
			}
		}
		
	}



}
