package berry.dispatch.handler;

import java.util.concurrent.TimeUnit;

import berry.dispatch.heartbeat.HeartbeatRequestTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartbeatSendHandler extends ChannelOutboundHandlerAdapter {

	private final long MIN_HEARTBEAT_REQUEST_PERIOD_SECOND = 3;

	private final long heartbeatPeriodSecond;
	
	private ScheduledFuture<?> scheduleAtFixedRate;

	public HeartbeatSendHandler(long heartbeatPeriodSecond) {
		if (heartbeatPeriodSecond < 0) {
			heartbeatPeriodSecond = MIN_HEARTBEAT_REQUEST_PERIOD_SECOND;
		}

		this.heartbeatPeriodSecond = heartbeatPeriodSecond;
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		
		this.scheduleAtFixedRate = ctx.executor().scheduleAtFixedRate(new HeartbeatRequestTask(ctx), 0, heartbeatPeriodSecond, TimeUnit.SECONDS);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		
		this.scheduleAtFixedRate.cancel(false);
	}
	
}
