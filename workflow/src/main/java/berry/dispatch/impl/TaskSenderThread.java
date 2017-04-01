package berry.dispatch.impl;

import java.util.concurrent.BlockingQueue;

import berry.db.po.WorkflowInstanceBean;
import berry.dispatch.InstanceSender;

public class TaskSenderThread implements Runnable {

	private BlockingQueue<WorkflowInstanceBean> workflowQueue;

	private BlockingQueue<WorkflowInstanceBean> failedWorkflowQueue;

	private InstanceSender instanceSender;

	public TaskSenderThread(BlockingQueue<WorkflowInstanceBean> workflowQueue,
			BlockingQueue<WorkflowInstanceBean> failedWorkflowQueue, InstanceSender instanceSender) {

		this.workflowQueue = workflowQueue;
		this.failedWorkflowQueue = failedWorkflowQueue;
		this.instanceSender = instanceSender;
	}

	@Override
	public void run() {
		
		while (true){
			
			WorkflowInstanceBean workflowInstance = null;
			
			try {
				workflowInstance = workflowQueue.take();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				break;
			}
			
			if (workflowInstance != null) {
				
				try {
					instanceSender.send(workflowInstance);
				} catch (Exception e) {
					e.printStackTrace();
					failedWorkflowQueue.offer(workflowInstance);
				}
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
