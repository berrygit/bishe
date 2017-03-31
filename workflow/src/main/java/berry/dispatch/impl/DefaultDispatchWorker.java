package berry.dispatch.impl;

import berry.dispatch.DispatchWorker;
import berry.dispatch.RpcWorkerService;

/**
 * Created by john on 2017/3/21.
 */
public class DefaultDispatchWorker implements DispatchWorker {
	
	private RpcWorkerService rpcWorkerService = new DefaultRpcWorkerService();

	@Override
	public void start() {
	}

	@Override
	public void stop() {

	}

	@Override
	public void changeLeader(String leader) {
		
		
		
		// TODO Auto-generated method stub
		
	}
}
