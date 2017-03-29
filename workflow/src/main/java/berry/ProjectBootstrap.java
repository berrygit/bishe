package berry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import berry.engine.WorkflowEngineImpl;
import berry.engine.model.StepTaskModel;

public class ProjectBootstrap {

	public static void main(String[] args) throws Exception {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/application-context.xml");

		WorkflowEngineImpl service = (WorkflowEngineImpl) ctx.getBean("workflowEngine");

		StepTaskModel model = new StepTaskModel();

		model.setMaxRetry(100);

		service.execute(null);

		// System.out.println(Date);
		//
	}

}
