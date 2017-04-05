package berry.engine.invoke;

import berry.api.WorkflowContext;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.model.interfaces.Task;

public interface InvokeStrategy {

	WorkflowContext invoke(WorkflowInstanceBean instance, Task task, WorkflowContext context) throws Exception;

}
