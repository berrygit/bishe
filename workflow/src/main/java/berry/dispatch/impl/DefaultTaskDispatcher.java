package berry.dispatch.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.dispatch.InstanceSender;
import berry.dispatch.TaskDispatcher;
import berry.dispatch.WorkerManager;
import berry.dispatch.common.LocalCache;

@Component
public class DefaultTaskDispatcher implements TaskDispatcher {
	
	@Value("${workflow.master.poolsize}")
	private int poolsize;
	
	@Value("${workflow.dispatch.node.minite.flowlimit}")
	private int flowlimit;
	
	@Resource
	private InstanceSender instanceSender;
	
	@Resource
	private WorkerManager workerManager;
	
	@Resource
	private WorkflowInstanceDao workflowInstanceDao;
	
	private ExecutorService executorService;
	
	private BlockingQueue<WorkflowInstanceBean> workflowQueue;

	private BlockingQueue<WorkflowInstanceBean> failedWorkflowQueue;
	
	private RetrySenderThread retrySenderThread;
	
	private TaskSenderThread taskSenderThread;
	
	private TaskPullThread taskPullThread;
	
	@PostConstruct
	public void init() {
		if (poolsize <= 2) {
			throw new RuntimeException("poolsize too small!");
		}
		this.executorService = Executors.newFixedThreadPool(poolsize);
		
		this.failedWorkflowQueue = new LinkedBlockingQueue<WorkflowInstanceBean>();
		this.retrySenderThread = new RetrySenderThread(instanceSender, failedWorkflowQueue);
		
		this.workflowQueue = new LinkedBlockingQueue<WorkflowInstanceBean>(1000);
		this.taskSenderThread = new TaskSenderThread(workflowQueue, failedWorkflowQueue, instanceSender);
		
		this.taskPullThread = new TaskPullThread(workflowInstanceDao, workflowQueue, flowlimit, workerManager);
		
	}

	@Override
	public void start() {
		
		this.workflowQueue.clear();
		this.failedWorkflowQueue.clear();
		
		this.executorService.execute(retrySenderThread);
		this.executorService.execute(taskSenderThread);
		this.executorService.execute(taskPullThread);

	}

	@Override
	public void stop() {
		
		executorService.shutdownNow();
		executorService = null;
		
		LocalCache.clear();

	}

}
