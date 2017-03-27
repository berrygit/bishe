package dispatch.handler;

import dispatch.po.HeartBeatMessage;
import io.netty.channel.ChannelHandlerContext;

public class HeartbeatRequestTask implements Runnable {

	private final ChannelHandlerContext ctx;

	private static final HeartBeatMessage message = new HeartBeatMessage("hello!");

	public HeartbeatRequestTask(ChannelHandlerContext ctx) {
		this.ctx = ctx;
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
