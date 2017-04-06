package berry.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import berry.api.WorkflowContext;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.InvokeStrategy;
import berry.engine.model.interfaces.Instance;
import berry.engine.model.interfaces.RollbackTask;

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
		Instance workflowInstance = workflowMetaInfo.getInstanceInfo(instance.getWorkflowName());
		
		if(workflowInstance == null) {
			System.out.println("can't find workflow info");
			return;
		}
		
		RollbackTask rollbackTask = workflowInstance.getRollbackTask();
		
		
		if(rollbackTask == null) {
			System.out.println("no rollback task, no need execute");
			return;
		}
		
		WorkflowContext context = JSON.parseObject(instance.getInitInfo(), WorkflowContext.class);

		try {
			taskInvokeStrategy.invoke(instance, rollbackTask, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
