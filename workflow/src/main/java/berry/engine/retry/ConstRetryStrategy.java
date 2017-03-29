package berry.engine.retry;

import berry.api.WorkflowContext;
import berry.common.exception.RetryMaxException;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.invoke.SteptaskInvokeStrategy;
import berry.engine.model.interfaces.StepTask;
import berry.engine.retry.interfaces.RetryStrategy;

public class ConstRetryStrategy implements RetryStrategy {

	private long currentRetry = 0;

	@Override
	public WorkflowContext retry(SteptaskInvokeStrategy steptaskInvokeStrategy, WorkflowInstanceBean instance, StepTask stepTask, WorkflowContext context) throws Exception {

		if (currentRetry < stepTask.getMaxRetry()) {

			currentRetry++;

			Thread.sleep(stepTask.getRetryIntervalMlis());

			try {
				return steptaskInvokeStrategy.invoke(instance, stepTask, context);
			} catch (Exception e) {
				return retry(steptaskInvokeStrategy, instance, stepTask, context);
			}
		}

		throw new RetryMaxException();
	}

}
