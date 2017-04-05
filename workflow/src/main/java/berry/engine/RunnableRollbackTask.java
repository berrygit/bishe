package berry.engine;

import com.alibaba.fastjson.JSON;

import berry.api.WorkflowContext;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.InvokeStrategy;
import berry.engine.model.interfaces.Instance;
import berry.engine.model.interfaces.RollbackTask;

public class RunnableRollbackTask implements Runnable {

	private WorkflowInstanceBean instance;

	private WorkflowMetaInfo workflowMetaInfo;

	private InvokeStrategy taskInvokeStrategy;

	public RunnableRollbackTask(WorkflowInstanceBean instance, WorkflowMetaInfo workflowMetaInfo,
			InvokeStrategy taskInvokeStrategy) {

		this.instance = instance;
		this.taskInvokeStrategy = taskInvokeStrategy;
		this.workflowMetaInfo = workflowMetaInfo;

	}

	public void run() {
		Instance workflowInstance = workflowMetaInfo.getInstanceInfo(instance.getWorkflowName());
		RollbackTask rollbackTask = workflowInstance.getRollbackTask();
		WorkflowContext context = JSON.parseObject(instance.getInitInfo(), WorkflowContext.class);

		try {
			taskInvokeStrategy.invoke(instance, rollbackTask, context);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
