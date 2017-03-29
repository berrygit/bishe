package berry.api.imp;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import berry.api.WorkflowContext;
import berry.api.WorkflowService;
import berry.common.Constant;
import berry.common.enums.WorkflowInstanceState;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.WorkflowEngine;
import berry.engine.WorkflowMetaInfo;

@Component("workflowService")
public class WorkflowServiceImpl implements WorkflowService {

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Resource
	private WorkflowMetaInfo workflowMetaInfo;

	@Resource
	private WorkflowEngine workflowEngine;

	@Override
	public void executeWorkflow(String requestId, String workflowName, Object request) {

		WorkflowInstanceBean instance = storeWorkflowInstance(requestId, workflowName, request);

		workflowEngine.execute(instance);

	}

	@Override
	public void scheduleWorkflow(String requestId, String workflowName, Object request) {

		storeWorkflowInstance(requestId, workflowName, request);
	}

	private WorkflowInstanceBean storeWorkflowInstance(String requestId, String workflowName, Object request) {

		WorkflowInstanceBean instance = new WorkflowInstanceBean();

		instance.setRequestId(requestId);
		instance.setWorkflowName(workflowName);

		// 序列化请求信息
		WorkflowContext context = new WorkflowContext();
		context.getContext().put("request", request);
		instance.setInitInfo(JSON.toJSONString(context));

		instance.setStatus(WorkflowInstanceState.INIT.name());

		Long timeoutMils = workflowMetaInfo.getInstanceInfo(workflowName).getTimeoutMils();

		if (timeoutMils != null && timeoutMils.longValue() > 0) {
			instance.setTimeoutMils(timeoutMils.longValue());
		} else {
			instance.setTimeoutMils(Constant.DEFAULT_TIMEOUT_MILS);
		}

		instance.setGmtBegin(new Date());

		workflowInstanceDao.createInstance(instance);

		return instance;
	}

	@Override
	public String queryResult(String requestId, String workflowName) {

		WorkflowInstanceBean instance = new WorkflowInstanceBean();

		instance.setRequestId(requestId);
		instance.setWorkflowName(workflowName);

		WorkflowInstanceBean instanceBean = workflowInstanceDao.getInstanceByRequestIdAndWorkflowName(instance);

		return instanceBean.getStatus();
	}

}
