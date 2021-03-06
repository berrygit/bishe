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
import berry.engine.model.interfaces.Task;

@Component
public class NoTimeoutInvokeStrategy implements InvokeStrategy {

	@Resource
	private WorkflowTaskDao workflowTaskDao;

	@Override
	public WorkflowContext invoke(WorkflowInstanceBean instance, Task task, WorkflowContext context) throws Exception {

		if (task == null) {
			System.out.println("task is empty");
			throw new IllegalStateException("task is empty");
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
