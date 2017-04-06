package berry.engine.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import berry.api.WorkflowContext;
import berry.engine.model.interfaces.RollbackTask;

public class RollbackTaskModel implements RollbackTask {

	private Object entity;

	private Method method;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	public void setInvokeMetaInfo(String method, Object entity) throws NoSuchMethodException, SecurityException {
		
		this.entity = entity;
		this.method = this.entity.getClass().getMethod(method, WorkflowContext.class);
	}

	@Override
	public Map<String, Object> invoke(WorkflowContext context) throws Exception {
		
		try {
			this.method.invoke(this.entity, context);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException t) {
			throw (Exception)t.getTargetException();
		}
		
		return null;
	}

	@Override
	public String getStepName() {
		return "rollback";
	}

}
