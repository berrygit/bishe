package berry.engine.retry;

import java.util.Map;

import berry.api.WorkflowContext;
import berry.common.exception.RetryMaxException;
import berry.engine.model.interfaces.StepTask;
import berry.engine.retry.interfaces.RetryStrategy;

public class IndexRetryStrategy implements RetryStrategy {

	private long currentRetry = 0;

	@Override
	public Map<String, Object> retry(StepTask stepTask, WorkflowContext context) throws Exception {

		if (currentRetry < stepTask.getMaxRetry()) {

			Thread.sleep((long) (Math.pow(2, currentRetry) * stepTask.getRetryIntervalMlis()));

			currentRetry++;

			try {
				return stepTask.invoke(context);
			} catch (Throwable e) {
				return retry(stepTask, context);
			}
		}

		throw new RetryMaxException();
	}

}
