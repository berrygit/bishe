package berry.dispatch.heartbeat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.ChannelHandlerContext;

public class HeartbeatCheckTask implements Runnable {
	
	private final long timeoutMils;
	
	private final ChannelHandlerContext ctx;
	
	private AtomicLong lastUpdateTime;
	
	private AtomicBoolean alive;
	
	public HeartbeatCheckTask(AtomicLong lastUpdateTime, AtomicBoolean alive, long timeoutMils, ChannelHandlerContext ctx) {
		
		this.lastUpdateTime = lastUpdateTime;
		this.alive = alive;
		this.timeoutMils = timeoutMils;
		this.ctx = ctx;
	}

	@Override
	public void run() {
		
		if (lastUpdateTime.get() + timeoutMils < System.currentTimeMillis()) {
			
			this.alive.set(false);
			ctx.fireUserEventTriggered(new LostHeartbeatEvent());
		}
	}

}
