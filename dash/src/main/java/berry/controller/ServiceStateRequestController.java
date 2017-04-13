package berry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import berry.dao.ServiceStateDAO;

@Controller
@RequestMapping("/service")
public class ServiceStateRequestController {
	
	private ServiceStateDAO serviceStateDAO; 

    @RequestMapping(value = "/service/query", method = RequestMethod.GET)
    public String getstateInfo() {
    	serviceStateDAO.getStateInfoOneDay();
		return null;
    }
    
    @RequestMapping(value = "/service/detail", method = RequestMethod.GET)
    public String queryServiceDetail() {
    	serviceStateDAO.getStateInfoOneDayByService(null);
		return null;
    }
}
