package dispatch.handler;

import java.io.Serializable;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class HeartbeatOutboundHandler extends ChannelOutboundHandlerAdapter {

	private final long MIN_HEARTBEAT_REQUEST_PERIOD_SECOND = 3;

	private final long heartbeatPeriodSecond;

	public HeartbeatOutboundHandler(long heartbeatPeriodSecond) {
		if (heartbeatPeriodSecond < 0) {
			heartbeatPeriodSecond = MIN_HEARTBEAT_REQUEST_PERIOD_SECOND;
		}

		this.heartbeatPeriodSecond = heartbeatPeriodSecond;

	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

		Serializable s = ctx.toString();
		System.out.println(s);
		ctx.writeAndFlush(s);

		// ctx.executor().scheduleAtFixedRate(new HeartbeatRequestTask(ctx), 0,
		// heartbeatPeriodSecond, TimeUnit.SECONDS);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println(ctx);
		super.write(ctx, msg, promise);
	}

}
