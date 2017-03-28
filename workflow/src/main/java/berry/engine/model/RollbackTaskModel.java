package berry.engine.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import berry.api.WorkflowContext;
import berry.engine.model.interfaces.RollbackTask;

public class RollbackTaskModel implements RollbackTask {

	private String action;

	private Object entity;

	@Override
	public Map<String, Object> invoke(WorkflowContext context) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
