package berry.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.common.exception.NotFindFlowException;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.InvokeStrategy;
import berry.engine.invoke.NoTimeoutInvokeStrategy;
import berry.engine.model.interfaces.Instance;

@Component("workflowEngine")
public class WorkflowEngineImpl implements WorkflowEngine {

	@Resource
	private WorkflowMetaInfo workflowMetaInfo;

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Resource
	private InvokeStrategy taskInvokeStrategy;
	
	@Resource
	private NoTimeoutInvokeStrategy noTimeoutInvokeStrategy;

	private ExecutorService pool = Executors.newFixedThreadPool(40);

	@Override
	public void execute(WorkflowInstanceBean instance) throws NotFindFlowException {
		
		Instance instancemetaInfo = workflowMetaInfo.getInstanceInfo(instance.getWorkflowName());
		
		pool.submit(new RunnableWorkflowTask(instance, instancemetaInfo, workflowInstanceDao, taskInvokeStrategy));
	}

	@Override
	public void rollback(WorkflowInstanceBean instance) throws NotFindFlowException {
		
		Instance instancemetaInfo = workflowMetaInfo.getInstanceInfo(instance.getWorkflowName());
		
		new RunnableRollbackTask(instance, instancemetaInfo, noTimeoutInvokeStrategy).run();
	}
	
	@Override
	public void executeForHumanRollback(WorkflowInstanceBean instance) throws NotFindFlowException {
		
		Instance instancemetaInfo = workflowMetaInfo.getInstanceInfo(instance.getWorkflowName());
		
		pool.submit(new RunnableRollbackTask(instance, instancemetaInfo, noTimeoutInvokeStrategy));
	}
	
	@Override
	public void executeForHumanRetry(WorkflowInstanceBean instance) throws NotFindFlowException {
		
		Instance instancemetaInfo = workflowMetaInfo.getInstanceInfo(instance.getWorkflowName());
		
		pool.submit(new RunnableWorkflowTask(instance, instancemetaInfo, workflowInstanceDao, noTimeoutInvokeStrategy));
	}
	
}
