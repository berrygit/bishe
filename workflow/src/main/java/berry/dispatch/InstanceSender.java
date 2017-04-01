package berry.dispatch;

import berry.db.po.WorkflowInstanceBean;
import berry.dispatch.common.NosuitableWorkerException;
import berry.dispatch.common.RpcFailedException;

public interface InstanceSender {

	void send(WorkflowInstanceBean workflowInstance) throws NosuitableWorkerException, RpcFailedException;

}
