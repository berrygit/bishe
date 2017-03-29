package berry.db.dao;

import berry.db.po.WorkflowInstanceBean;

public interface WorkflowInstanceDao {

	void createInstance(WorkflowInstanceBean instance);

	WorkflowInstanceBean getInstance(WorkflowInstanceBean instance);
	
	WorkflowInstanceBean getInstanceByRequestIdAndWorkflowName(WorkflowInstanceBean instance);

	void updateStatus(WorkflowInstanceBean instance);

}
