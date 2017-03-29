package berry.engine;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import berry.api.WorkflowContext;
import berry.common.enums.WorkflowInstanceState;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.SteptaskInvokeStrategy;
import berry.engine.model.interfaces.Instance;
import berry.engine.model.interfaces.RollbackTask;
import berry.engine.model.interfaces.StepTask;
import berry.engine.retry.RetryStrategyFactory;
import berry.engine.retry.interfaces.RetryStrategy;

@Component("workflowEngine")
public class WorkflowEngineImpl implements WorkflowEngine {

	@Resource
	private WorkflowMetaInfo workflowMetaInfo;

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Resource
	private SteptaskInvokeStrategy steptaskInvokeStrategy;

	@Override
	public void execute(WorkflowInstanceBean instance) {

		Instance workflowInstance = workflowMetaInfo.getInstanceInfo(instance.getWorkflowName());

		if (instance.getGmtBegin().getTime() + workflowInstance.getTimeoutMils() > System.currentTimeMillis()) {
			List<StepTask> stepTaskList = workflowInstance.getStepTaskList();
			RollbackTask rollbackTask = workflowInstance.getRollbackTask();
			WorkflowContext context = JSON.parseObject(instance.getInitInfo(), WorkflowContext.class);
			
			try {

				for (StepTask stepTask : stepTaskList) {

					try {
						context = steptaskInvokeStrategy.invoke(instance, stepTask, context);
					} catch (Exception e) {

						RetryStrategy retryStrategy = RetryStrategyFactory.get(stepTask.getRetryStrategy());
						retryStrategy.retry(steptaskInvokeStrategy, instance, stepTask, context);
					}
				}
				
				instance.setStatus(WorkflowInstanceState.FINISH.name());
				
				workflowInstanceDao.updateStatus(instance);

			} catch (Exception e) {
				
				// 回滚
				try {
					rollbackTask.invoke(context);
					
					instance.setStatus(WorkflowInstanceState.FAILED.name());
					
					workflowInstanceDao.updateStatus(instance);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
