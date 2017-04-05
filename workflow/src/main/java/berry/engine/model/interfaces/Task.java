package berry.engine.model.interfaces;

import java.util.Map;

import berry.api.WorkflowContext;

public interface Task {
	
	String getStepName();

	Map<String, Object> invoke(WorkflowContext context) throws Exception;
	
	void setInvokeMetaInfo(String action, Object entity) throws NoSuchMethodException, SecurityException;
	
}
