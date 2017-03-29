package berry.db.dao;

import berry.db.po.WorkflowInstanceBean;

public interface WorkflowInstanceDao {

	void createInstance(WorkflowInstanceBean instance);

	WorkflowInstanceBean getStatus(WorkflowInstanceBean instance);

	void updateStatus(WorkflowInstanceBean instance);

	void updateEndTime(WorkflowInstanceBean instance);

	void updateCurrentStep(WorkflowInstanceBean instance);
}
