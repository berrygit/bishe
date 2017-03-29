package berry.engine.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import berry.api.WorkflowContext;
import berry.engine.model.interfaces.RollbackTask;

public class RollbackTaskModel implements RollbackTask {

	private String action;

	private Object entity;

	private Method method;

	public String getAction() {
		return action;
	}

	public Object getEntity() {
		return entity;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	public void setActionAndEntity(String action, Object entity) throws NoSuchMethodException, SecurityException {
		this.action = action;
		this.entity = entity;

		this.method = this.entity.getClass().getMethod(action, WorkflowContext.class);
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

}
