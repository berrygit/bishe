package berry.engine.invoke;

import java.net.InetAddress;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import berry.api.WorkflowContext;
import berry.db.dao.WorkflowTaskDao;
import berry.db.po.WorkflowInstanceBean;
import berry.db.po.WorkflowTaskBean;
import berry.engine.TimeoutChecker;
import berry.engine.model.interfaces.StepTask;
import berry.engine.model.interfaces.Task;

@Component
public class DefaultInvokeStrategy implements InvokeStrategy {

	@Resource
	private WorkflowTaskDao workflowTaskDao;

	@Override
	public WorkflowContext invoke(WorkflowInstanceBean instance, Task task, WorkflowContext context) throws Exception {

		// 只对正常任务进行超时校验
		if (task instanceof StepTask){
			TimeoutChecker.check(instance.getTimeoutMils(), instance.getGmtBegin());
		}
		
		WorkflowTaskBean workflowTaskBean = new WorkflowTaskBean();
		
		Map<String, Object> map;

		try {

			workflowTaskBean.setTaskName(task.getStepName());
			workflowTaskBean.setWorkflowId(instance.getId());
			workflowTaskBean.setInput(instance.getInitInfo());
			workflowTaskBean.setNode(InetAddress.getLocalHost().getHostAddress());

			workflowTaskDao.createTask(workflowTaskBean);

			map = task.invoke(context);

			if (map != null){
				context.getContext().putAll(map);
			}
			
			workflowTaskBean.setOutput(JSON.toJSONString(context));

			workflowTaskDao.finishTask(workflowTaskBean);

		} catch (Exception e) {
			
			String message = e.getMessage();

			if (StringUtils.isEmpty(message)) {
				workflowTaskBean.setExcetionMessage(e.toString());
			} else{
				workflowTaskBean.setExcetionMessage(message);
			}

			workflowTaskDao.recordFailedInfo(workflowTaskBean);
			
			throw e;
		}
		
		return context;
	}

}
