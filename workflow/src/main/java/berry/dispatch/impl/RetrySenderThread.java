package berry.dispatch.impl;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import berry.db.po.WorkflowInstanceBean;
import berry.dispatch.InstanceSender;

public class RetrySenderThread implements Runnable {

	private BlockingQueue<WorkflowInstanceBean> failedWorkflowQueue;

	private InstanceSender instanceSender;

	public RetrySenderThread(InstanceSender instanceSender, BlockingQueue<WorkflowInstanceBean> failedWorkflowQueue) {
		this.instanceSender = instanceSender;
		this.failedWorkflowQueue = failedWorkflowQueue;
	}

	public void run() {

		while (true) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}

			boolean interupt = false;

			for (Iterator<WorkflowInstanceBean> iterator = failedWorkflowQueue.iterator(); iterator.hasNext();) {
				WorkflowInstanceBean workflowInstance = iterator.next();

				try {

					instanceSender.send(workflowInstance);
					iterator.remove();

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					interupt = true;
				}

				if (interupt) {
					break;
				}
			}

			if (interupt) {
				break;
			}

		}

	}

}