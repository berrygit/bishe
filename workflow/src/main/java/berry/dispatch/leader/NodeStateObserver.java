package berry.dispatch.leader;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.Participant;

import berry.dispatch.common.LocalAddress;

public class NodeStateObserver implements PathChildrenCacheListener {

	private LeaderLatch leaderLatch;

	@Resource
	private RelationshipProcessor relationshipProcessor;

	private String lastLeaderId = "";

	private final String selfNodeId = LocalAddress.getIp();
	
	public void setLeaderLatch(LeaderLatch leaderLatch) {
		this.leaderLatch = leaderLatch;
	}

	private void changeNodeRole() throws InterruptedException {
		
		String leaderId = "";

		do {
			try {
				Participant leader = leaderLatch.getLeader();
				leaderId = leader.getId();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(100);
		} while (StringUtils.isEmpty(leaderId));

		if (!leaderId.equals(lastLeaderId)) {
			lastLeaderId = leaderId;
			relationshipProcessor.changeLeader(leaderId);
			if (this.selfNodeId.equals(leaderId)) {
				relationshipProcessor.beLeader();
			} else {
				// 幂等
				relationshipProcessor.lostLeader();
			}
		}
	}

	@Override
	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {

		PathChildrenCacheEvent.Type type = event.getType();

		switch (type) {
		case CHILD_ADDED:
		case CHILD_UPDATED:
		case CHILD_REMOVED:
			changeNodeRole();
			break;
		case CONNECTION_SUSPENDED:
		case CONNECTION_LOST:
			// 停止所有服务
			relationshipProcessor.stop();
			break;
		case CONNECTION_RECONNECTED:
		case INITIALIZED:
			// 启动
			relationshipProcessor.start();
			lastLeaderId="";
			changeNodeRole();
			break;
		default:
			throw new RuntimeException("not support event");
		}
	}

}
