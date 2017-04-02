package berry.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import berry.api.WorkflowContext;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.SteptaskInvokeStrategy;
import berry.engine.model.interfaces.Instance;
import berry.engine.model.interfaces.RollbackTask;

@Component("workflowEngine")
public class WorkflowEngineImpl implements WorkflowEngine {

	@Resource
	private WorkflowMetaInfo workflowMetaInfo;

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Resource
	private SteptaskInvokeStrategy steptaskInvokeStrategy;
	
	private ExecutorService pool = Executors.newFixedThreadPool(40);
	
	@Override
	public void execute(WorkflowInstanceBean instance) {
		
		pool.submit(new RunnableTask(instance, workflowInstanceDao, workflowMetaInfo, steptaskInvokeStrategy));

	}
	
	@Override
	public void rollback(WorkflowInstanceBean instance) throws Exception {
		Instance workflowInstance = workflowMetaInfo.getInstanceInfo(instance.getWorkflowName());
		RollbackTask rollbackTask = workflowInstance.getRollbackTask();
		WorkflowContext context = JSON.parseObject(instance.getInitInfo(), WorkflowContext.class);
		
		rollbackTask.invoke(context);
	}

}
