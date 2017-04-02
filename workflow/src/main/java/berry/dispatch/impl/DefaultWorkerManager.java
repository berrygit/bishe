package berry.dispatch.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.dispatch.PushStrategy;
import berry.dispatch.WorkerManager;
import berry.dispatch.common.NosuitableWorkerException;
import io.netty.channel.Channel;

@Component
public class DefaultWorkerManager implements WorkerManager{

    private final Map<String, Channel> workerMap = new ConcurrentHashMap<String, Channel>();
    
    @Resource
    private PushStrategy pushStrategy;

	@Override
	public Channel getSuitableWorker() throws NosuitableWorkerException {
		
		if (workerMap.isEmpty()) {
			throw new NosuitableWorkerException();
		}
		
		List<String> nodes = new ArrayList<String>();
		
		for (String node : workerMap.keySet()){
			nodes.add(node);
		}
		
		String worker = pushStrategy.getTargetWorker(nodes);
		
		return workerMap.get(worker);
	}

	@Override
	public void removeWorker(String key) {
		workerMap.remove(key);
	}
	
	@Override
	public void addWorker(String key, Channel channel) {
		workerMap.put(key, channel);
	}
	
	@Override
	public void clear(){
		workerMap.clear();
	}

	@Override
	public int getWorkerCount() {
		return workerMap.size();
	}

}
