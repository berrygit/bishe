package berry.dao;

import berry.model.CallStaticsBean;

public interface CallStatisticsDAO {

	void addInfoFiveMinuteAgo(CallStaticsBean bean);

	void queryStatisticsBeforeTime(String time);

	void addScheduleInfo(long taskCount);

}
