package berry.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import berry.dao.CallStatisticsDAO;
import berry.dao.WorkflowInstanceDAO;
import berry.enums.WorkflowInstanceState;
import berry.model.CallStaticsBean;
import berry.model.CallStaticsInfo;

public class CallStatisticsJob {

	@Resource
	private WorkflowInstanceDAO workflowInstanceDAO;

	@Resource
	private CallStatisticsDAO callStatisticsDAO;

	@Scheduled(cron = "0 * * * * ? *")
	public void loadCallStatisticsInfo() {
		List<CallStaticsInfo> callStaticsInfos = workflowInstanceDAO.getCallStaticsInfoByMinite();
		
		CallStaticsBean bean = new CallStaticsBean();

		for (CallStaticsInfo callStaticsInfo : callStaticsInfos) {

			if (WorkflowInstanceState.FINISH.name().equals(callStaticsInfo.getStatus())) {
				bean.setSuccessCount(callStaticsInfo.getCount());
			}
			
			if (WorkflowInstanceState.FAILED.name().equals(callStaticsInfo.getStatus())) {
				bean.setFailedCount(callStaticsInfo.getCount());
			}
			
			if (WorkflowInstanceState.TIMEOUT.name().equals(callStaticsInfo.getStatus())) {
				bean.setTimeoutCount(callStaticsInfo.getCount());
			}

			callStatisticsDAO.addInfoFiveMinuteAgo(bean);
		}
		
		long taskCount = workflowInstanceDAO.getScheduleTaskCount();
		
		callStatisticsDAO.addScheduleInfo(taskCount);
	}
}
