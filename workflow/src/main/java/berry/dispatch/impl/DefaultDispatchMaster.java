package berry.dispatch.impl;

import berry.dispatch.DispatchMaster;
import berry.dispatch.RpcMasterService;
import berry.dispatch.TaskDispatcher;

public class DefaultDispatchMaster implements DispatchMaster {
	
	private boolean running = false;
	
	private RpcMasterService rpcMasterService;
	
	private TaskDispatcher taskDispatcher;

	@Override
	public synchronized void start() {
		
		if (running){
			return;
		}
		
		boolean started = false;
		
		while (true){
			
			try{
				rpcMasterService.start();
				started = true;
			}catch (Exception e) {
			}
			
			if (started) {
				break;
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		
		taskDispatcher.start();
		
	}

	@Override
	public synchronized void stop() {
		
		if (!running) {
			return;
		}
		
		taskDispatcher.stop();
		try {
			rpcMasterService.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}