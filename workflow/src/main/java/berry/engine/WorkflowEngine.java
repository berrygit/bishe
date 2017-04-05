package berry.engine;

import berry.db.po.WorkflowInstanceBean;

public interface WorkflowEngine {

	void execute(WorkflowInstanceBean instance);

	void rollback(WorkflowInstanceBean instance);

}
