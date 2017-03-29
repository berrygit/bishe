package berry.api;

public interface WorkflowService {

	void executeWorkflow(String requestId, String workflowName, Object request);

	void scheduleWorkflow(String requestId, String workflowName, Object request);

	String queryResult(String requestId, String workflowName);

}
