package berry.db.dao;

import berry.db.po.WorkflowTaskBean;

public interface WorkflowTaskDao {

	void createTask(WorkflowTaskBean task);

	void finishTask(WorkflowTaskBean task);
	
	void recordFailedInfo(WorkflowTaskBean task);

}
