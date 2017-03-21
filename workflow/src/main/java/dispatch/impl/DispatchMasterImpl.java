package dispatch.impl;

import dispatch.DispatchMaster;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by john on 2017/3/21.
 */
public class DispatchMasterImpl implements DispatchMaster {

	private final int port;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	DispatchMasterImpl(int port) {
		this.port = port;
	}

	public void start() {

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);

		bootstrap.bind(port);

	}

	public void stop() {

		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}

		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
		}

	}
}
