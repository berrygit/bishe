package berry.dispatch.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import berry.dispatch.FailOverManager;
import berry.dispatch.TaskDispatcher;
import berry.dispatch.po.WorkflowInstance;

@Component
public class DefaultTaskDispatcher implements TaskDispatcher {
	
	@Value("${workflow.master.poolsize}")
	private int poolsize;
	
	private ExecutorService executorService;
	
	private BlockingQueue<WorkflowInstance> workflowQueue;

	private BlockingQueue<WorkflowInstance> failedWorkflowQueue;
	
	private FailOverManager failOverManager;
	
	private RetrySenderThread retrySenderThread;
	
	public void init() {
		if (poolsize <= 2) {
			throw new RuntimeException("poolsize too small!");
		}
		
		this.executorService = Executors.newFixedThreadPool(poolsize);
		
		this.workflowQueue = new LinkedBlockingQueue<WorkflowInstance>(1000);
		
		
		
		this.failOverManager = new DefaultFailOverManager();
		
		this.retrySenderThread = new RetrySenderThread();
		
		this.failedWorkflowQueue = new LinkedBlockingQueue<WorkflowInstance>();
		this.retrySenderThread.setFailedWorkflowQueue(failedWorkflowQueue);
	}

	@Override
	public void start() {
		
		this.workflowQueue.clear();
		this.failedWorkflowQueue.clear();
		
		

		
		

	}
	

	@Override
	public void stop() {
		
		executorService.shutdownNow();
		executorService = null;

	}

}
