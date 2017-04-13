package berry.engine.retry;

import com.alibaba.fastjson.JSON;

import berry.api.WorkflowContext;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.InvokeStrategy;
import berry.engine.model.interfaces.Instance;
import berry.engine.model.interfaces.RollbackTask;

public class RunnableRollbackTask implements Runnable {

	private WorkflowInstanceBean instance;

	private Instance workflowInstance;

	private InvokeStrategy taskInvokeStrategy;

	public RunnableRollbackTask(WorkflowInstanceBean instance, Instance workflowInstance,
			InvokeStrategy taskInvokeStrategy) {

		this.instance = instance;
		this.workflowInstance = workflowInstance;
		this.taskInvokeStrategy = taskInvokeStrategy;

	}

	public void run() {

		RollbackTask rollbackTask = workflowInstance.getRollbackTask();

		if (rollbackTask == null) {
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
