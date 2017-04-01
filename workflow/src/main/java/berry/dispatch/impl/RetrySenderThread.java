package berry.dispatch.impl;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.aliyun.grandcanal.schedule.master.ConnectedSlaveManager;
import com.aliyun.grandcanal.schedule.master.TaskSender;
import com.aliyun.grandcanal.schedule.master.WrappedTask;

import berry.dispatch.SlaveManager;
import berry.dispatch.po.WorkflowInstance;

@Component
public class RetrySenderThread implements Runnable {
	
	private BlockingQueue<WorkflowInstance> failedWorkflowQueue;

	@Resource
	private SlaveManager slaveManager;
	
	
	public void run() {

		while (true) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
			
	        for (Iterator<WorkflowInstance> iterator = failedWorkflowQueue.iterator(); iterator.hasNext();) {
	        	WorkflowInstance workflowInstance = iterator.next();
	        	
	        	try {
	        	
	                //TaskSender sender = slaveManager.getSuitableSender(null);
	                
	                //if (trySend(workflowInstance, sender)) {
	                    iterator.remove();
	                //}
	        	}catch (Exception e) {
	        		e.printStackTrace();
				}
	            
	            try {
					Thread.sleep(5);
				} catch (InterruptedException e) {

				}
	        }
			
		}

	}
	
	
	public void setFailedWorkflowQueue(BlockingQueue<WorkflowInstance> failedWorkflowQueue) {
		this.failedWorkflowQueue = failedWorkflowQueue;
	}
}