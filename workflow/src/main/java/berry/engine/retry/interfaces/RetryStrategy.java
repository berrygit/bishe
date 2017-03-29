package berry.engine.retry.interfaces;

import java.util.Map;

import berry.api.WorkflowContext;
import berry.engine.model.interfaces.StepTask;

public interface RetryStrategy {

	Map<String, Object> retry(StepTask stepTask, WorkflowContext context) throws Exception;

}
