package berry.dispatch.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Exchanger;


public class LocalCache {

	private static Map<String, Exchanger<String>> cache = new ConcurrentHashMap<String, Exchanger<String>>();
	
	public static void clear() {
		cache.clear();
	}
	
	public static Exchanger<String> get(String key) {
		return cache.get(key);
	}
	
	public static void delete(String key) {
		cache.remove(key);
	}
	
	public static void put(String key, Exchanger<String> exchanger){
		cache.put(key, exchanger);
	}
}
