package berry.dispatch.impl;

import java.util.UUID;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.db.po.WorkflowInstanceBean;
import berry.dispatch.InstanceSender;
import berry.dispatch.WorkerManager;
import berry.dispatch.common.LocalCache;
import berry.dispatch.common.NosuitableWorkerException;
import berry.dispatch.common.RpcFailedException;
import berry.dispatch.enums.ResponseState;
import berry.dispatch.po.RpcCallMessage;
import io.netty.channel.Channel;

@Component
public class DefaultInstanceSender implements InstanceSender {
	
	@Resource
	private WorkerManager workerManager;
	
	@Override
	public void send(WorkflowInstanceBean workflowInstance) throws NosuitableWorkerException, RpcFailedException {
		
		Channel sender = workerManager.getSuitableWorker();
		
		if (sender != null && sender.isWritable()) {
			
			RpcCallMessage message = new RpcCallMessage();
			
			message.setWorkflowID(workflowInstance.getId());
			String messageId = UUID.randomUUID().toString();
			Exchanger<String> exchanger = new Exchanger<String>();
			LocalCache.put(messageId, exchanger);
			
			sender.writeAndFlush(message);
			
			String result = "";
			
			try {
				result = exchanger.exchange("", 3, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				LocalCache.delete(messageId);
				throw new RpcFailedException();
			}
			
			if (!ResponseState.SUCCESS.name().equals(result)){
				throw new RpcFailedException();
			}
		} else {
			throw new NosuitableWorkerException();
		}
	}

}
