package berry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import berry.api.WorkflowService;
import berry.engine.model.StepTaskModel;

public class ProjectBootstrap {

	public static void main(String[] args) throws Exception {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/application-context.xml");

		WorkflowService service = (WorkflowService) ctx.getBean("workflowService");

		StepTaskModel model = new StepTaskModel();

		model.setMaxRetry(100);
		
		service.queryResult("12345", "StaffEntry");

		// System.out.println(Date);
		//
	}

}
