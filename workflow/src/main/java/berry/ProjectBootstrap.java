package berry;

import berry.dispatch.impl.DefaultRpcMasterService;

public class ProjectBootstrap {

	public static void main(String[] args) throws Exception {

//		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/application-context.xml");
//		WorkflowService service = (WorkflowService) ctx.getBean("workflowService");

		DefaultRpcMasterService rpcService = new DefaultRpcMasterService(8080);
		
		rpcService.start();
	}

}
