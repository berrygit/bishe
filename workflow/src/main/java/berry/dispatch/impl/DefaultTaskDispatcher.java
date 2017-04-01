package berry.dispatch.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import berry.db.po.WorkflowInstanceBean;
import berry.dispatch.FailOverManager;
import berry.dispatch.InstanceSender;
import berry.dispatch.TaskDispatcher;
import berry.dispatch.common.LocalCache;

@Component
public class DefaultTaskDispatcher implements TaskDispatcher {
	
	@Value("${workflow.master.poolsize}")
	private int poolsize;
	
	@Resource
	private InstanceSender instanceSender;
	
	private ExecutorService executorService;
	
	private BlockingQueue<WorkflowInstanceBean> workflowQueue;

	private BlockingQueue<WorkflowInstanceBean> failedWorkflowQueue;
	
	private FailOverManager failOverManager;
	
	private RetrySenderThread retrySenderThread;
	
	private TaskSenderThread taskSenderThread;
	
	public void init() {
		if (poolsize <= 2) {
			throw new RuntimeException("poolsize too small!");
		}
		this.executorService = Executors.newFixedThreadPool(poolsize);
		
		this.failedWorkflowQueue = new LinkedBlockingQueue<WorkflowInstanceBean>();
		this.retrySenderThread = new RetrySenderThread(instanceSender, failedWorkflowQueue);
		
		this.workflowQueue = new LinkedBlockingQueue<WorkflowInstanceBean>(1000);
		this.taskSenderThread = new TaskSenderThread(workflowQueue, failedWorkflowQueue, instanceSender);
		
		
		
		this.failOverManager = new DefaultFailOverManager();
	}

	@Override
	public void start() {
		
		this.workflowQueue.clear();
		this.failedWorkflowQueue.clear();
		
		this.executorService.execute(retrySenderThread);
		this.executorService.execute(taskSenderThread);

	}
	

	@Override
	public void stop() {
		
		executorService.shutdownNow();
		executorService = null;
		
		LocalCache.clear();

	}

}
