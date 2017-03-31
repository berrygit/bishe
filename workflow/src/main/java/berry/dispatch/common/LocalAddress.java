package berry.dispatch.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalAddress {
	
	public static String getIp(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "nukonwn host";
		}
	}

}
