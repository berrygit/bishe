package berry.dispatch.leader;

import berry.dispatch.DispatchMaster;
import berry.dispatch.DispatchWorker;
import berry.dispatch.impl.DefaultDispatchMaster;
import berry.dispatch.impl.DefaultDispatchWorker;

public class DefaultRelationshipProcessor implements RelationshipProcessor {

	private DispatchMaster master;

	private DispatchWorker worker;
	
	public DefaultRelationshipProcessor(int port) {
		this.master = new DefaultDispatchMaster(port);
		this.worker = new DefaultDispatchWorker();
	}

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
