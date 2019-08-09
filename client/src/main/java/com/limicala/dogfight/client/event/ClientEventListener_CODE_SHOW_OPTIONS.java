package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.print.SimpleWriter;
import com.limicala.dogfight.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice(ConsoleMessage.OPTIONS);
		String line = SimpleWriter.write("options");
		
		while(line == null || OptionsUtils.getOptions(line) == -1) {
			SimplePrinter.printNotice(ConsoleMessage.INVALID_OPTION);
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
			SimplePrinter.printNotice(ConsoleMessage.INVALID_OPTION);
			call(channel, data);
		}
		
	}



}
