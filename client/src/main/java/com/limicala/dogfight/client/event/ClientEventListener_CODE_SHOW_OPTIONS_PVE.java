package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.enums.ServerEventCode;
import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.print.SimpleWriter;
import com.limicala.dogfight.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS_PVE extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice("PVE: ");
		SimplePrinter.printNotice("1. Simple Model");
		SimplePrinter.printNotice("2. Medium Model");
		SimplePrinter.printNotice("3. Difficulty Model");
		SimplePrinter.printNotice("Please enter the number of options (enter [BACK] return options list)");
		String line = SimpleWriter.write("pve");
		
		if(line.equalsIgnoreCase("BACK")) {
			get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
		}else {

			while(line == null || OptionsUtils.getOptions(line) == -1) {
				SimplePrinter.printNotice("Invalid option, please choose again：");
				line = SimpleWriter.write("pve");
			}
			
			int choose = Integer.valueOf(line);
			
			if(0 < choose && choose < 4) {
				pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE_PVE, String.valueOf(choose));
			}else {
				SimplePrinter.printNotice("Invalid option, please choose again：");
				call(channel, data);
			}
		}
		
	}



}
