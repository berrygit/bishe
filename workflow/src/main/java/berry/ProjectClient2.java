package berry;

import berry.dispatch.impl.DefaultRpcWorkerService;

public class ProjectClient2 {

	public static void main(String[] args) throws Exception {

		DefaultRpcWorkerService rpcService = new DefaultRpcWorkerService();
		
		rpcService.start("127.0.0.1", 8080);
		
		Thread.sleep(10000);
		
		rpcService.disconnect();
	}
}
