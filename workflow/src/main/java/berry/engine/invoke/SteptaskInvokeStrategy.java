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

@Component
public class SteptaskInvokeStrategy implements InvokeStrategy {

	@Resource
	private WorkflowTaskDao workflowTaskDao;

	@Override
	public WorkflowContext invoke(WorkflowInstanceBean instance, StepTask stepTask, WorkflowContext context) throws Exception {

		TimeoutChecker.check(instance.getTimeoutMils(), instance.getGmtBegin());
		
		WorkflowTaskBean task = new WorkflowTaskBean();
		
		Map<String, Object> map;

		try {

			task.setTaskName(stepTask.getStepName());
			task.setWorkflowId(instance.getId());
			task.setInput(instance.getInitInfo());
			task.setNode(InetAddress.getLocalHost().getHostAddress());

			workflowTaskDao.createTask(task);

			map = stepTask.invoke(context);

			if (map != null){
				context.getContext().putAll(map);
			}
			
			task.setOutput(JSON.toJSONString(context));

			workflowTaskDao.finishTask(task);

		} catch (Exception e) {
			
			String message = e.getMessage();

			if (StringUtils.isEmpty(message)) {
				task.setExcetionMessage(e.toString());
			} else{
				task.setExcetionMessage(message);
			}

			workflowTaskDao.recordFailedInfo(task);
			
			throw e;
		}
		
		return context;
	}

}
