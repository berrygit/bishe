package berry.engine;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import berry.api.WorkflowContext;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.model.interfaces.Instance;
import berry.engine.model.interfaces.RollbackTask;
import berry.engine.model.interfaces.StepTask;

@Component("workflowEngine")
public class WorkflowEngineImpl implements WorkflowEngine {

	@Resource
	private WorkflowMetaInfo workflowMetaInfo;

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Override
	public void execute(WorkflowInstanceBean instance) {

		WorkflowInstanceBean bean = new WorkflowInstanceBean();

		bean.setWorkflowName("StaffEntry");
		bean.setRequestId("1231234");
		bean.setStatus("TimeOut");
		bean.setGmtEnd(new Date());

		workflowInstanceDao.updateEndTime(bean);

		Instance workflowInstance = workflowMetaInfo.getInstanceInfo(instance.getWorkflowName());

		if (instance.getGmtBegin().getTime() + workflowInstance.getTimeoutMils() > System.currentTimeMillis()) {
			List<StepTask> stepTaskList = workflowInstance.getStepTaskList();
			RollbackTask rollbackTask = workflowInstance.getRollbackTask();

			for (StepTask stepTask : stepTaskList) {

				WorkflowContext context = JSON.parseObject(instance.getInitInfo(), WorkflowContext.class);

				try {
					stepTask.invoke(context);
				} catch (Throwable e) {
				}
			}

		} else {

		}

	}

}
