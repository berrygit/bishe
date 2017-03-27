package dispatch.impl;

import dispatch.DispatchMaster;
import dispatch.RpcMasterService;

/**
 * Created by john on 2017/3/21.
 */
public class DefaultDispatchMaster implements DispatchMaster {

	private RpcMasterService rpcMasterService;

	DefaultDispatchMaster(int port) {
		rpcMasterService = new DefaultRpcMasterService(port);
	}

	public void start() {
		try {
			rpcMasterService.start();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			rpcMasterService.stop();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
