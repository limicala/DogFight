package com.limicala.dogfight.client.event;

import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.print.SimpleWriter;
import com.limicala.dogfight.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice("Options: ");
		SimplePrinter.printNotice("1. PvP");
		SimplePrinter.printNotice("2. PvE");
		SimplePrinter.printNotice("3. Setting");
		SimplePrinter.printNotice("Please enter the number of options");
		String line = SimpleWriter.write("options");
		
		while(line == null || OptionsUtils.getOptions(line) == -1) {
			SimplePrinter.printNotice("Invalid option, please choose again：");
			line = SimpleWriter.write("option");
		}
		
		int choose = Integer.valueOf(line);
		
		if(choose == 1) {
			get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
		}else if(choose == 2){
			get(ClientEventCode.CODE_SHOW_OPTIONS_PVE).call(channel, data);
		}else if(choose == 3){
			get(ClientEventCode.CODE_SHOW_OPTIONS_SETTING).call(channel, data);
		}else {
			SimplePrinter.printNotice("Invalid option, please choose again：");
			call(channel, data);
		}
		
	}



}
