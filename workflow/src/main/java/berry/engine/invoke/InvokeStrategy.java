package berry.engine.invoke;

import berry.api.WorkflowContext;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.model.interfaces.StepTask;

public interface InvokeStrategy {

	WorkflowContext invoke(WorkflowInstanceBean instance, StepTask stepTask, WorkflowContext context) throws Exception;

}
