package berry.engine.retry.interfaces;

import berry.api.WorkflowContext;
import berry.common.exception.TimeoutException;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.SteptaskInvokeStrategy;
import berry.engine.model.interfaces.StepTask;

public abstract class AbstractRetryStrategy implements RetryStrategy{

	protected WorkflowContext invoke(SteptaskInvokeStrategy steptaskInvokeStrategy, WorkflowInstanceBean instance, StepTask stepTask, WorkflowContext context) throws Exception{
		
		try {
			return steptaskInvokeStrategy.invoke(instance, stepTask, context);
		} catch (Exception e) {
			
			if (e instanceof TimeoutException){
				throw e;
			}else {
				return retry(steptaskInvokeStrategy, instance, stepTask, context);
			}
		}
	}

}
