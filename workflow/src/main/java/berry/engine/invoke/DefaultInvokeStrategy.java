package berry.engine.invoke;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.api.WorkflowContext;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.TimeoutChecker;
import berry.engine.model.interfaces.Task;

@Component
public class DefaultInvokeStrategy implements InvokeStrategy {

	@Resource
	private NoTimeoutInvokeStrategy noTimeoutInvokeStrategy;

	@Override
	public WorkflowContext invoke(WorkflowInstanceBean instance, Task task, WorkflowContext context) throws Exception {

		TimeoutChecker.check(instance.getTimeoutMils(), instance.getGmtBegin());

		return noTimeoutInvokeStrategy.invoke(instance, task, context);
	}
}
