package com.limicala.dogfight.server;

import com.limicala.dogfight.print.SimplePrinter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.limicala.dogfight.server.handler.DefaultChannelInitializer;
import com.limicala.dogfight.server.timer.RoomClearTask;

import java.net.InetSocketAddress;
import java.util.Timer;

public class SimpleServer {

	public static void main(String[] args) throws InterruptedException {
		
		if(args != null && args.length > 1) {
			if(args[0].equalsIgnoreCase("-p") || args[0].equalsIgnoreCase("-port")) {
				ServerContains.port = Integer.valueOf(args[1]);
			}
		}
		
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap()
			.group(parentGroup, childGroup)
			.channel(NioServerSocketChannel.class)
			.localAddress(new InetSocketAddress(ServerContains.port))
			.childHandler(new DefaultChannelInitializer());
			
			ChannelFuture f = bootstrap .bind().sync();
			
			SimplePrinter.serverLog("The server was successfully started on port " + ServerContains.port);
			
			ServerContains.THREAD_EXCUTER.execute(() -> {
				Timer timer=new Timer();
				timer.schedule(new RoomClearTask(), 0L, 5000L);
			});
			f.channel().closeFuture().sync();
		} finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
		
		
	}
}
