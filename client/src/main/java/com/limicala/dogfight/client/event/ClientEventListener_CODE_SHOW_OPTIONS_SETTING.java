package com.limicala.dogfight.client.event;

import com.limicala.dogfight.message.ConsoleMessage;
import io.netty.channel.Channel;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.helper.PokerHelper;
import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.print.SimpleWriter;
import com.limicala.dogfight.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS_SETTING extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.printNotice(ConsoleMessage.SETTING);
		String line = SimpleWriter.write("setting");
		
		if(line.equalsIgnoreCase("BACK")) {
			get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
		}else {
			while(line == null || OptionsUtils.getOptions(line) == -1) {
				SimplePrinter.printNotice(ConsoleMessage.INVALID_SETTING);
				line = SimpleWriter.write("setting");
			}
			
			int choose = Integer.valueOf(line);
			if(choose >=1 && choose <= 5){
				PokerHelper.pokerPrinterType = choose - 1;
				get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
			} else {
				SimplePrinter.printNotice(ConsoleMessage.INVALID_SETTING);
				call(channel, data);
			}
		}
	}



}
