package berry.dispatch.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import berry.dispatch.PushStrategy;

@Component
public class RoundRobinPushStrategy implements PushStrategy{
	
	private int index = 0;

	@Override
	public String getTargetWorker(List<String> worker) {
		
		if (worker == null || worker.isEmpty()){
			return null;
		}
		
		if (index < worker.size()){
			return worker.get(index++);
		}else{
			index = 0;
			return worker.get(index++);
		}
	}

}
