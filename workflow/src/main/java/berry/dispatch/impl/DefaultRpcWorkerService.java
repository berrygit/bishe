package berry.dispatch.impl;

import berry.dispatch.handler.HeartbeatOutboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class DefaultRpcWorkerService {

	private EventLoopGroup workerGroup;

	private String host;

	private int port;

	private volatile Channel channel;

	private final ChannelFutureListener unexpectDisconnectListener = new ChannelFutureListener() {

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			start(host, port);
		}
	};

	public synchronized void start(String host, int port) throws Exception {

		this.host = host;
		this.port = port;

		this.stop();

		this.workerGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new ObjectDecoder(
										ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())))
								.addLast(new ObjectEncoder()).addLast(new HeartbeatOutboundHandler(5));
					}
				});

		ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

		if (channelFuture.isSuccess()) {
			channel = channelFuture.channel();
			channelFuture.channel().closeFuture().addListener(unexpectDisconnectListener);
		}

	}

	public synchronized void stop() {
		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
			workerGroup = null;
		}
	}

	public void sendMessage(Object message) throws Exception {

		if (channel != null && channel.isWritable()) {
			channel.writeAndFlush(message).sync();
		} else {
			throw new Exception("channel is not avaliable");
		}

	}

}
