package berry.dispatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import berry.dispatch.leader.RelationshipManager;
import berry.dispatch.leader.ZkRelationshipManager;

@Component
public class DispatchManager {
	
	@Value("${workflow.zk.host}")
	private String zkHost;
	
	@Value("${workflow.zk.path}")
	private String zkPath;
	
	@Value("${workflow.engine.port}")
	private int port;
	
	private RelationshipManager relationshipManager;
	
	@PostConstruct
	public void init() throws Exception{
		
		relationshipManager = new ZkRelationshipManager(zkHost, zkPath, port);
		
		relationshipManager.start();
	}
	
	@PreDestroy
	public void stop() throws Exception {
		relationshipManager.stop();
	}

}
