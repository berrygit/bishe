package berry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import berry.dao.NodeInfoDao;

@Controller
@RequestMapping("/node")
public class NodeInfoRequestController {
	
	private NodeInfoDao nodeInfoDao;

    @RequestMapping(value = "/node/add", method = RequestMethod.GET)
    public String add() {
    	
    	nodeInfoDao.insert(null);
    	
    	return null;
    }
    
    @RequestMapping(value = "/node/update", method = RequestMethod.GET)
    public String update() {
    	
    	nodeInfoDao.update(null);
    	
    	return null;
    }
    
    @RequestMapping(value = "/node/delete", method = RequestMethod.GET)
    public String delete() {
    	
    	nodeInfoDao.delete(null);
    	
    	return null;
    }
    
    @RequestMapping(value = "/node/query", method = RequestMethod.GET)
    public String query() {
    	
    	nodeInfoDao.query();
    	
    	return null;
    }
}
