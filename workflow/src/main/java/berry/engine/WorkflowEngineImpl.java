package berry.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.InvokeStrategy;

@Component("workflowEngine")
public class WorkflowEngineImpl implements WorkflowEngine {

	@Resource
	private WorkflowMetaInfo workflowMetaInfo;

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Resource
	private InvokeStrategy taskInvokeStrategy;

	private ExecutorService pool = Executors.newFixedThreadPool(40);

	@Override
	public void execute(WorkflowInstanceBean instance) {
		pool.submit(new RunnableWorkflowTask(instance, workflowInstanceDao, workflowMetaInfo, taskInvokeStrategy));
	}

	@Override
	public void rollback(WorkflowInstanceBean instance) {
		pool.submit(new RunnableRollbackTask(instance, workflowMetaInfo, taskInvokeStrategy));
	}

}
