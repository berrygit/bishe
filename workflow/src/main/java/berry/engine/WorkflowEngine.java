package berry.engine;

import berry.common.exception.NotFindFlowException;
import berry.db.po.WorkflowInstanceBean;

public interface WorkflowEngine {

	void execute(WorkflowInstanceBean instance) throws NotFindFlowException;

	void executeForHumanRollback(WorkflowInstanceBean instance) throws NotFindFlowException;

	void executeForHumanRetry(WorkflowInstanceBean instance) throws NotFindFlowException;

	void rollback(WorkflowInstanceBean instance) throws NotFindFlowException;

}
