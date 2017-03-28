package berry.engine.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import berry.api.WorkflowContext;
import berry.engine.model.interfaces.StepTask;

public class StepTaskModel implements StepTask {

	private String action;

	private Object entity;

	private int maxRetry;

	private int retryIntervalMlis;

	private String retryStrategy;

	private Method method;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> invoke(WorkflowContext context) throws Throwable {

		Map<String, Object> result = null;

		try {
			result = (Map<String, Object>) method.invoke(this.entity, context);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException t) {
			throw t.getTargetException();
		}

		return result;

	}

	public String getAction() {
		return action;
	}

	public void setActionAndEntity(String action, Object entity) throws NoSuchMethodException, SecurityException {
		this.action = action;
		this.entity = entity;

		this.method = this.entity.getClass().getMethod(action, WorkflowContext.class);
	}

	public Object getEntity() {
		return entity;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	public int getRetryIntervalMlis() {
		return retryIntervalMlis;
	}

	public void setRetryIntervalMlis(int retryIntervalMlis) {
		this.retryIntervalMlis = retryIntervalMlis;
	}

	public String getRetryStrategy() {
		return retryStrategy;
	}

	public void setRetryStrategy(String retryStrategy) {
		this.retryStrategy = retryStrategy;
	}

	@Override
	public String getStepName() {
		return action;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
