package berry.dispatch.heartbeat;

import java.net.InetAddress;
import java.net.UnknownHostException;

import berry.dispatch.po.HeartBeatMessage;
import io.netty.channel.ChannelHandlerContext;

public class HeartbeatRequestTask implements Runnable {

	private final ChannelHandlerContext ctx;

	private HeartBeatMessage message;

	public HeartbeatRequestTask(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		try {
			this.message = new HeartBeatMessage(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			this.message = new HeartBeatMessage("");
		}
	}

	@Override
	public void run() {
		
		if (ctx != null && ctx.channel() != null && ctx.channel().isWritable()) {
			
			try {
				ctx.channel().writeAndFlush(message);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
