package berry.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import berry.dao.NodeInfoDao;
import berry.dao.WorkflowInstanceDAO;
import berry.model.NodeStateCount;

public class NodeStateJob {

	@Resource
	private WorkflowInstanceDAO workflowInstanceDAO;
	
	@Resource
	private NodeInfoDao nodeInfoDAO;

	@Scheduled(cron = "0 30 0/1 * * ? *")
	public void loadNodeStateInfo() {
		
		List<NodeStateCount> countInfos = workflowInstanceDAO.getNodeStateCountInfo();
		
		for (NodeStateCount nodeState : countInfos) {
			nodeInfoDAO.update(null);
		}
		
	}
}
