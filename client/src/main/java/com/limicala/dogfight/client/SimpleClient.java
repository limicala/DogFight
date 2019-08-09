package com.limicala.dogfight.client;

import com.limicala.dogfight.print.SimplePrinter;
import com.limicala.dogfight.print.SimpleWriter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import com.limicala.dogfight.client.handler.DefaultChannelInitializer;
import com.limicala.dogfight.utils.StreamUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SimpleClient {

	public static int id = -1;
	
	public static String serverAddress = null;
	
	public static int port = -1;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		if(args != null && args.length > 0) {
			for(int index = 0; index < args.length; index = index + 2) {
				if(index + 1 < args.length) {
					if(args[index].equalsIgnoreCase("-p") || args[index].equalsIgnoreCase("-port")) {
						port = Integer.valueOf(args[index + 1]);
					}
					if(args[index].equalsIgnoreCase("-h") || args[index].equalsIgnoreCase("-host")) {
						serverAddress = args[index + 1];
					}
				}
			}
		}
		
		if(serverAddress == null || port == 0){
			String serverInfo = StreamUtils.convertToString(new URL("https://github.com/limicala/dogfight/blob/master/serverlist.json"));
			List<String> serverAddressList = Noson.convert(serverInfo, new NoType<List<String>>() {});
			SimplePrinter.printNotice("Please select a server:");
			for(int i = 0; i < serverAddressList.size(); i++) {
				SimplePrinter.printNotice((i+1) + ". " + serverAddressList.get(i));
			}
			int serverPick = 0;
			while(serverPick<1 || serverPick>serverAddressList.size()){
				try {
					serverPick = Integer.parseInt(SimpleWriter.write("option"));
				}catch(NumberFormatException e){}
			}
			serverAddress = serverAddressList.get(serverPick-1);
			String[] elements = serverAddress.split(":");
			serverAddress = elements[0];
			port = Integer.parseInt(elements[1]);
		}
		
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap()
					.group(group)
					.channel(NioSocketChannel.class)
					.handler(new DefaultChannelInitializer());
			SimplePrinter.printNotice("Connecting to " + serverAddress + ":" + port);
			Channel channel = bootstrap.connect(serverAddress, port).sync().channel();
			channel.closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
}
