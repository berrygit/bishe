package berry.dispatch.leader;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.dispatch.DispatchMaster;
import berry.dispatch.DispatchWorker;

@Component
public class DefaultRelationshipProcessor implements RelationshipProcessor {

	@Resource
	private DispatchMaster master;

	@Resource
	private DispatchWorker worker;
	
	@Override
	public void beLeader() {
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
		master.stop();
		worker.stop();
	}
	
	@Override
	public void start(){
		worker.start();
	}

}
