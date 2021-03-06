package berry.engine;

import java.util.List;

import com.alibaba.fastjson.JSON;

import berry.api.WorkflowContext;
import berry.common.enums.WorkflowInstanceState;
import berry.common.exception.TimeoutException;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.InvokeStrategy;
import berry.engine.model.interfaces.Instance;
import berry.engine.model.interfaces.RollbackTask;
import berry.engine.model.interfaces.StepTask;
import berry.engine.retry.RetryStrategyFactory;
import berry.engine.retry.interfaces.RetryStrategy;

public class RunnableWorkflowTask implements Runnable {

	private WorkflowInstanceBean instance;

	private Instance workflowInstance;

	private WorkflowInstanceDao workflowInstanceDao;

	private InvokeStrategy taskInvokeStrategy;

	public RunnableWorkflowTask(WorkflowInstanceBean instance, Instance workflowInstance,
			WorkflowInstanceDao workflowInstanceDao, InvokeStrategy taskInvokeStrategy) {

		this.instance = instance;
		this.workflowInstance = workflowInstance;
		this.taskInvokeStrategy = taskInvokeStrategy;
		this.workflowInstanceDao = workflowInstanceDao;

	}

	public void run() {

		// 更新状态
		instance.setStatus(WorkflowInstanceState.RUNNING.name());
		workflowInstanceDao.updateStatus(instance);

		// 更新创建时间等信息
		instance = workflowInstanceDao.getInstance(instance);

		List<StepTask> stepTaskList = workflowInstance.getStepTaskList();
		RollbackTask rollbackTask = workflowInstance.getRollbackTask();
		WorkflowContext context = JSON.parseObject(instance.getInitInfo(), WorkflowContext.class);
		WorkflowContext initContext = context;

		try {

			for (StepTask stepTask : stepTaskList) {

				try {
					context = taskInvokeStrategy.invoke(instance, stepTask, context);
				} catch (Exception e) {

					e.printStackTrace();

					RetryStrategy retryStrategy = RetryStrategyFactory.get(stepTask.getRetryStrategy());
					retryStrategy.retry(taskInvokeStrategy, instance, stepTask, context);
				}
			}

			instance.setStatus(WorkflowInstanceState.FINISH.name());
			workflowInstanceDao.updateStatus(instance);

		} catch (Exception e) {

			if (e instanceof TimeoutException) {

				// 超时
				instance.setStatus(WorkflowInstanceState.TIMEOUT.name());
				workflowInstanceDao.updateStatus(instance);

			} else {
				instance.setStatus(WorkflowInstanceState.FAILED.name());
				workflowInstanceDao.updateStatus(instance);
			}

			if (rollbackTask == null) {
				return;
			}

			// 回滚
			try {
				taskInvokeStrategy.invoke(instance, rollbackTask, initContext);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
