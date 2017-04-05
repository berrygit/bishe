package berry.engine.retry;

import berry.api.WorkflowContext;
import berry.common.exception.RetryMaxException;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.InvokeStrategy;
import berry.engine.model.interfaces.StepTask;
import berry.engine.retry.interfaces.AbstractRetryStrategy;
import berry.engine.retry.interfaces.RetryStrategy;

public class StepRetryStrategy extends AbstractRetryStrategy implements RetryStrategy {

	private long currentRetry = 0;

	@Override
	public WorkflowContext retry(InvokeStrategy taskInvokeStrategy, WorkflowInstanceBean instance, StepTask stepTask,
			WorkflowContext context) throws Exception {

		if (currentRetry < stepTask.getMaxRetry()) {

			currentRetry++;

			Thread.sleep(stepTask.getRetryIntervalMlis() * currentRetry);

			this.invoke(taskInvokeStrategy, instance, stepTask, context);
		}

		throw new RetryMaxException();
	}

}
