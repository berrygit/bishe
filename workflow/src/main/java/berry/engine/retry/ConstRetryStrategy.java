package berry.engine.retry;

import java.util.Map;

import berry.api.WorkflowContext;
import berry.common.exception.RetryMaxException;
import berry.engine.model.interfaces.StepTask;
import berry.engine.retry.interfaces.RetryStrategy;

public class ConstRetryStrategy implements RetryStrategy {

	private long currentRetry = 0;

	@Override
	public Map<String, Object> retry(StepTask stepTask, WorkflowContext context) throws Exception {

		if (currentRetry < stepTask.getMaxRetry()) {

			currentRetry++;

			Thread.sleep(stepTask.getRetryIntervalMlis());

			try {
				return stepTask.invoke(context);
			} catch (Throwable e) {
				return retry(stepTask, context);
			}
		}

		throw new RetryMaxException();
	}

}
