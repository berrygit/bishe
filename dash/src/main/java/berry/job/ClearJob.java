package berry.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

public class ClearJob {

	@Scheduled(cron = "0,30 * * * * ? *")
	public void clear(){
		
	}
	
}
