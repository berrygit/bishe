package berry.engine;

import berry.db.po.WorkflowInstanceBean;

public interface WorkflowEngine {

	void execute(WorkflowInstanceBean instance) throws Exception;

	void executeForHumanRollback(WorkflowInstanceBean instance) throws Exception;

	void executeForHumanRetry(WorkflowInstanceBean instance) throws Exception;

	void rollback(WorkflowInstanceBean instance) throws Exception;

}
