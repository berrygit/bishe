package berry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import berry.dao.CallStatisticsDAO;

@Controller
@RequestMapping("/statistics")
public class CallStatisticsRequestController {
	
	private CallStatisticsDAO callStatisticsDAO;

    @RequestMapping(value = "/statistics/hour", method = RequestMethod.GET)
    public String getStatisticsByHour() {
    	callStatisticsDAO.queryStatisticsBeforeTime(null);
		return null;
    }
    
    @RequestMapping(value = "/statistics/day", method = RequestMethod.GET)
    public String getStatisticsByDay() {
    	callStatisticsDAO.queryStatisticsBeforeTime(null);
		return null;
    }
    
    @RequestMapping(value = "/statistics/week", method = RequestMethod.GET)
    public String getStatisticsByWeek() {
    	callStatisticsDAO.queryStatisticsBeforeTime(null);
		return null;
    }
}
