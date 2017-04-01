package berry.dispatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.dispatch.leader.RelationshipManager;

@Component
public class DispatchManager {
	
	@Resource
	private RelationshipManager relationshipManager;
	
	@PostConstruct
	public void init() throws Exception{
		
		relationshipManager.start();
	}
	
	@PreDestroy
	public void stop() throws Exception {
		relationshipManager.stop();
	}

}
