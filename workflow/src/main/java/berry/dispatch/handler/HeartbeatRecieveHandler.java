package berry.dispatch.handler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import berry.dispatch.heartbeat.FindHeartbeatEvent;
import berry.dispatch.heartbeat.HeartbeatCheckTask;
import berry.dispatch.heartbeat.LostHeartbeatEvent;
import berry.dispatch.po.HeartBeatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartbeatRecieveHandler extends SimpleChannelInboundHandler<HeartBeatMessage> {

	private static final long MIN_TIMEOUT_MILS = 10;

	ScheduledFuture<?> heartbeatSchedule;

	private final long timeoutMils;

	private AtomicLong lastUpdateTime = new AtomicLong(0);

	private AtomicBoolean alive = new AtomicBoolean(false);

	public HeartbeatRecieveHandler(int timeoutSeconds) {
		this(timeoutSeconds, TimeUnit.SECONDS);
	}

	private HeartbeatRecieveHandler(int timeout, TimeUnit timeUnit) {

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
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		
		lastUpdateTime.set(System.currentTimeMillis());
		
		this.heartbeatSchedule = ctx.executor().scheduleAtFixedRate(new HeartbeatCheckTask(lastUpdateTime, alive, timeoutMils, ctx), 0,
				timeoutMils, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HeartBeatMessage msg) throws Exception {

		lastUpdateTime.set(System.currentTimeMillis());
		
		if (!alive.get()) {
			
			alive.set(true);
			
			ctx.fireUserEventTriggered(new FindHeartbeatEvent());
			
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		
		ctx.fireUserEventTriggered(new LostHeartbeatEvent());
		
		this.heartbeatSchedule.cancel(false);
	}

}
