package berry.engine.retry.interfaces;

import berry.api.WorkflowContext;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.SteptaskInvokeStrategy;
import berry.engine.model.interfaces.StepTask;

public interface RetryStrategy {

	WorkflowContext retry(SteptaskInvokeStrategy steptaskInvokeStrategy, WorkflowInstanceBean instance, StepTask stepTask, WorkflowContext context) throws Exception;

}
