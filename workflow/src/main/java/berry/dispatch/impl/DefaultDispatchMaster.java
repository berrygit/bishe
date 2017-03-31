package berry.dispatch.impl;

import berry.dispatch.DispatchMaster;
import berry.dispatch.RpcMasterService;

/**
 * Created by john on 2017/3/21.
 */
public class DefaultDispatchMaster implements DispatchMaster {

	private RpcMasterService rpcMasterService;

	public DefaultDispatchMaster(int port) {
		rpcMasterService = new DefaultRpcMasterService(port);
	}

	public void start(){
		try {
			rpcMasterService.start();
		} catch (Exception e) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void stop() {
		try {
			rpcMasterService.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
