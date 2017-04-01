package berry.dispatch.impl;

import berry.common.exception.ConnectionException;
import berry.dispatch.RpcWorkerService;
import berry.dispatch.handler.HeartbeatSendHandler;
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

public class DefaultRpcWorkerService implements RpcWorkerService{

	private EventLoopGroup workerGroup;

	private String host;

	private int port;

	private volatile Channel channel;
	
	private Bootstrap bootstrap;
	
	public DefaultRpcWorkerService() {

		this.workerGroup = new NioEventLoopGroup();
		this.bootstrap = new Bootstrap();

		bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new ObjectDecoder(
										ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())))
								.addLast(new ObjectEncoder())
								.addLast(new HeartbeatSendHandler(1));
					}
				});

	}

	private final ChannelFutureListener unexpectDisconnectListener = new ChannelFutureListener() {

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			
			start(host, port);
		}
	};

	public void start(String host, int port) throws Exception {

		this.host = host;
		this.port = port;
		
		disconnect();

		ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

		if (channelFuture.isSuccess()) {
			channel = channelFuture.channel();
			channelFuture.channel().closeFuture().addListener(unexpectDisconnectListener);
			
			return;
		}
		
		throw new ConnectionException();
	}

	public void stop() throws InterruptedException {
		if (workerGroup != null) {
			workerGroup.shutdownGracefully().sync();
		}
	}
	
	public void disconnect() throws InterruptedException {
		
        if (channel != null && channel.isOpen()) {
            channel.close().sync();
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
