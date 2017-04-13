package berry.dao;

import org.apache.ibatis.annotations.Mapper;

import berry.model.WorkflowInstanceBean;

public interface WorkflowInstanceBackupDAO {
	
	void insert(WorkflowInstanceBean bean);

}
