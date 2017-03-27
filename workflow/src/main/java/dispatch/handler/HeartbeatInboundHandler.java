package dispatch.handler;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartbeatInboundHandler extends ChannelInboundHandlerAdapter {

	private static final long MIN_TIMEOUT_MILS = 1;

	ScheduledFuture<?> heartbeatSchedule;

	private volatile boolean on = false;
	private final long timeoutMils;

	public HeartbeatInboundHandler(int timeoutSeconds) {
		this(timeoutSeconds, TimeUnit.SECONDS);
	}

	private HeartbeatInboundHandler(int timeout, TimeUnit timeUnit) {

		if (timeUnit == null) {
			throw new NullPointerException("TimeUnit");
		}

		if (timeout <= 0) {
			timeoutMils = MIN_TIMEOUT_MILS;
		} else {
			timeoutMils = timeUnit.toMillis(timeout);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg);
		super.channelRead(ctx, msg);
	}

	private void init(ChannelHandlerContext ctx) {
		if (on) {
			return;
		}

		on = true;

		EventExecutor executor = ctx.executor();

		heartbeatSchedule = executor.schedule(new HeartbeatCheckTask(), timeoutMils, TimeUnit.MILLISECONDS);

	}

	private void destroy() {

		on = false;

		if (heartbeatSchedule != null) {
			heartbeatSchedule.cancel(false);
			heartbeatSchedule = null;
		}
	}

}
