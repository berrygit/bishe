package berry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import berry.dao.WorkflowTaskDAO;

@Controller
@RequestMapping("/instance")
public class InstanceInfoRequestController {
	
	//private WorkflowInstanceDAO workflowInstanceDAO;
	
	private WorkflowTaskDAO workflowTaskDAO;
	

    @RequestMapping(value = "/instance/search", method = RequestMethod.GET)
    public String search() {
    	
    	//workflowInstanceDAO.queryWithLimit(null, null, null, null, null, null, null);
    	
    	return null;
    }
    
    @RequestMapping(value = "/instance/detail", method = RequestMethod.GET)
    public String detail() {
    	workflowTaskDAO.query(null);
    	
		return null;
    }
    
    @RequestMapping(value = "/instance/retry", method = RequestMethod.GET)
    public String retry() {
    	//workflowInstanceDAO.updateStatus(null, null);
		return null;
    }
    
    @RequestMapping(value = "/instance/rollback", method = RequestMethod.GET)
    public String rollback() {
    	
    	//workflowInstanceDAO.updateStatus(null, null);
		return null;
    }
}
