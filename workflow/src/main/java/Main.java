import dispatch.impl.DefaultRpcMasterService;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		new DefaultRpcMasterService(8080).start();
	}

}
