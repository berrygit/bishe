package berry.engine.retry.interfaces;

import berry.api.WorkflowContext;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.InvokeStrategy;
import berry.engine.model.interfaces.StepTask;

public interface RetryStrategy {

	WorkflowContext retry(InvokeStrategy steptaskInvokeStrategy, WorkflowInstanceBean instance, StepTask stepTask,
			WorkflowContext context) throws Exception;

}
