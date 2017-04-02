package berry.dispatch.impl;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import berry.dispatch.DispatchWorker;
import berry.dispatch.RpcWorkerService;

@Component
public class DefaultDispatchWorker implements DispatchWorker {

	@Value("${workflow.engine.port}")
	private int port;

	private RpcWorkerService rpcWorkerService = new DefaultRpcWorkerService();

	private final ScheduledExecutorService reconnectExecutor = Executors.newSingleThreadScheduledExecutor();

	private final AtomicLong currentVersion = new AtomicLong(0);
	private ScheduledFuture<?> retryFuture;

	private class ReconnectTask extends TimerTask {

		private final String leader;
		private final long version;

		private ReconnectTask(String leader, long version) {
			this.leader = leader;
			this.version = version;
		}

		@Override
		public void run() {

			if (this.version == currentVersion.get()) {
				try {
					rpcWorkerService.start(leader, port);
					return;
				} catch (Exception e) {
				}
				retryFuture = reconnectExecutor.schedule(this, 1, TimeUnit.SECONDS);
			}
		}
	}

	@Override
	public void stop() {
		if (retryFuture != null) {
			retryFuture.cancel(true);
		}

		try {
			rpcWorkerService.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void changeLeader(String leader) {

		if (retryFuture != null) {
			retryFuture.cancel(true);
		}

		retryFuture = reconnectExecutor.schedule(new ReconnectTask(leader, currentVersion.incrementAndGet()), 0,
				TimeUnit.SECONDS);

	}

}