package berry.dispatch.leader;

import java.net.InetAddress;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderLatch;

public class ZkRelationshipManager implements RelationshipManager{
	
	private static final String LATCH_PATH = "/latch_leader";
	
	private final LeaderLatch leaderLatch;
	
	private final CuratorFramework curatorFramework;
	
	private final PathChildrenCache pathChildrenCache;
	
	public ZkRelationshipManager(String host, String path, int port) throws Exception{
		
		this.curatorFramework = CuratorFrameworkFactory.builder().
                connectString(host).namespace(path).build();
		this.leaderLatch = new LeaderLatch(curatorFramework, LATCH_PATH, InetAddress.getLocalHost().getHostAddress());
		this.pathChildrenCache = new PathChildrenCache(curatorFramework, host, false);
		pathChildrenCache.getListenable().addListener(new NodeStateObserver(leaderLatch, port));
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
