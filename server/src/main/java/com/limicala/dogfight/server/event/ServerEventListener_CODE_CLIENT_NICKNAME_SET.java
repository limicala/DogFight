package com.limicala.dogfight.server.event;

import com.limicala.dogfight.channel.ChannelUtils;
import com.limicala.dogfight.entity.ClientSide;
import com.limicala.dogfight.enums.ClientEventCode;
import com.limicala.dogfight.helper.MapHelper;
import com.limicala.dogfight.server.ServerContains;

public class ServerEventListener_CODE_CLIENT_NICKNAME_SET implements ServerEventListener{

	public static final int NICKNAME_MAX_LENGTH = 10;
	
	@Override
	public void call(ClientSide client, String nickname) {
		
		if (nickname.trim().length() > NICKNAME_MAX_LENGTH) {
			String result = MapHelper.newInstance().put("invalidLength", nickname.trim().length()).json();
			ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_CLIENT_NICKNAME_SET, result);
		}else{
			ServerContains.CLIENT_SIDE_MAP.get(client.getId()).setNickname(nickname);
			ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_SHOW_OPTIONS, null);
		}
	}

}
