package berry.dispatch.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import berry.dispatch.RpcMasterService;
import berry.dispatch.handler.HeartbeatRecieveHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

@Component
public class DefaultRpcMasterService implements RpcMasterService {

	
	@Value("${workflow.engine.port}")
	private int port;
	
	private EventLoopGroup bossGroup;
	
	private EventLoopGroup workerGroup;

	@Override
	public void start() throws InterruptedException {

		// 幂等
		this.stop();

		this.bossGroup = new NioEventLoopGroup();
		this.workerGroup = new NioEventLoopGroup();

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new ObjectDecoder(
										ClassResolvers.softCachingConcurrentResolver(this.getClass().getClassLoader())))
								.addLast(new ObjectEncoder())
								.addLast(new HeartbeatRecieveHandler(10));
					}
				}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		bootstrap.bind(port).sync();

	}

	@Override
	public void stop() throws InterruptedException {

		if (bossGroup != null) {
			bossGroup.shutdownGracefully().sync();
		}

		if (workerGroup != null) {
			workerGroup.shutdownGracefully().sync();
		}
	}
}
