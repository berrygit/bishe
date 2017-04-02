package berry.dispatch.leader;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.db.dao.MasterInfoDao;
import berry.dispatch.DispatchMaster;
import berry.dispatch.DispatchWorker;
import berry.dispatch.common.LocalAddress;

@Component
public class DefaultRelationshipProcessor implements RelationshipProcessor {

	@Resource
	private DispatchMaster master;

	@Resource
	private DispatchWorker worker;
	
	@Resource
	private MasterInfoDao masterInfoDao;
	
	@Override
	public void beLeader() {
		masterInfoDao.update(LocalAddress.getIp());
		master.start();
	}

	@Override
	public void lostLeader() {
		master.stop();
	}

	@Override
	public void changeLeader(String leader) {
		worker.changeLeader(leader);
	}

	@Override
	public void stop() {
		worker.stop();
	}
	
}
