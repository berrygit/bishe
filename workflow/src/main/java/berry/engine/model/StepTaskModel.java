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

	private long maxRetry;

	private long retryIntervalMlis;

	private String retryStrategy;

	private Method method;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> invoke(WorkflowContext context) throws Exception {

		Map<String, Object> result = null;

		try {
			result = (Map<String, Object>) method.invoke(this.entity, context);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException t) {
			throw (Exception)t.getTargetException();
		}

		return result;
	}

	@Override
	public void setInvokeMetaInfo(String method, Object entity) throws NoSuchMethodException, SecurityException {
		this.entity = entity;
		this.method = this.entity.getClass().getMethod(method, WorkflowContext.class);
	}

	public long getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(long maxRetry) {
		this.maxRetry = maxRetry;
	}

	public long getRetryIntervalMlis() {
		return retryIntervalMlis;
	}

	public void setRetryIntervalMlis(long retryIntervalMlis) {
		this.retryIntervalMlis = retryIntervalMlis;
	}

	public String getRetryStrategy() {
		return retryStrategy;
	}

	public void setRetryStrategy(String retryStrategy) {
		this.retryStrategy = retryStrategy;
	}

	public void setAction(String action) {
		this.action = action;
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
