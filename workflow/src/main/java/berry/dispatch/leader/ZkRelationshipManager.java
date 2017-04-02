package berry.dispatch.leader;

import java.net.InetAddress;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZkRelationshipManager implements RelationshipManager{
	
	private static final String LATCH_PATH = "/latch_leader";
	
	private LeaderLatch leaderLatch;
	
	private CuratorFramework curatorFramework;
	
	private PathChildrenCache pathChildrenCache;
	
	private NodeStateObserver nodeStateObserver;
	
	@Value("${workflow.zk.host}")
	private String zkHost;
	
	@Value("${workflow.zk.path}")
	private String zkPath;
	
	@Resource
	private RelationshipProcessor relationshipProcessor;
	
	@PostConstruct
	public void init() throws Exception{
		
		this.curatorFramework = CuratorFrameworkFactory.builder().
                connectString(zkHost).namespace(zkPath).build();
		this.leaderLatch = new LeaderLatch(curatorFramework, LATCH_PATH, InetAddress.getLocalHost().getHostAddress());
		this.pathChildrenCache = new PathChildrenCache(curatorFramework, zkPath, false);
		this.nodeStateObserver = new NodeStateObserver(leaderLatch, relationshipProcessor);
		this.pathChildrenCache.getListenable().addListener(nodeStateObserver);
	}
	
	@Override
	public void start() throws Exception{
		curatorFramework.start();
		leaderLatch.start();
		pathChildrenCache.start();
	}

	@Override
	public void stop() throws Exception {
		pathChildrenCache.close();
		leaderLatch.close();
		curatorFramework.close();
	}

}
