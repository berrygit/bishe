package berry.engine.retry;

import berry.api.WorkflowContext;
import berry.common.exception.RetryMaxException;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.SteptaskInvokeStrategy;
import berry.engine.model.interfaces.StepTask;
import berry.engine.retry.interfaces.AbstractRetryStrategy;
import berry.engine.retry.interfaces.RetryStrategy;

public class ConstRetryStrategy extends AbstractRetryStrategy implements RetryStrategy {

	private long currentRetry = 0;

	@Override
	public WorkflowContext retry(SteptaskInvokeStrategy steptaskInvokeStrategy, WorkflowInstanceBean instance, StepTask stepTask, WorkflowContext context) throws Exception {

		if (currentRetry < stepTask.getMaxRetry()) {

			currentRetry++;

			Thread.sleep(stepTask.getRetryIntervalMlis());

			this.invoke(steptaskInvokeStrategy, instance, stepTask, context);
		}

		throw new RetryMaxException();
	}
	
}
