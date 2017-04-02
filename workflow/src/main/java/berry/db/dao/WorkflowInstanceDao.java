package berry.db.dao;

import java.util.List;

import berry.db.po.WorkflowInstanceBean;

public interface WorkflowInstanceDao {

	void createInstance(WorkflowInstanceBean instance);

	WorkflowInstanceBean getInstance(WorkflowInstanceBean instance);
	
	WorkflowInstanceBean getInstanceByRequestIdAndWorkflowName(WorkflowInstanceBean instance);

	void updateStatus(WorkflowInstanceBean instance);

	List<WorkflowInstanceBean> getScheduleInstance();

	List<WorkflowInstanceBean> getBeScheduleInstance();

	List<WorkflowInstanceBean> getRunningInstanceByNode(String worker);
	
	void updateStatusAndNodeInfoById(WorkflowInstanceBean instance);
	
	int updateStatusAndNodeInfoByIdAndNodeisEmpty(WorkflowInstanceBean instance);

}
