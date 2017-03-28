package berry.engine.model.interfaces;

import java.util.Map;

import berry.api.WorkflowContext;

public interface Task {

	Map<String, Object> invoke(WorkflowContext context) throws Throwable;

}
