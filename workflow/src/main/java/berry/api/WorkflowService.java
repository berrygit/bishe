package berry.api;

import berry.common.exception.NotFindFlowException;

public interface WorkflowService {

	void executeWorkflow(String requestId, String workflowName, Object request) throws NotFindFlowException;
	
	void scheduleWorkflow(String requestId, String workflowName, Object request) throws NotFindFlowException;

	String queryResult(String requestId);
}
