package berry.dispatch.leader;

public interface RelationshipProcessor {
	
	void beLeader();
	
	void lostLeader();
	
	void changeLeader(String leader);

	void stop();
	
}
