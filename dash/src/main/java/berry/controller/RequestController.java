package berry.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/")
public class RequestController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
    	
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    	
    	System.out.println(request);
    	
        return "/page/index";
    }
    
    @RequestMapping(value = "/page/{page}", method = RequestMethod.GET)
    public String pageProcess(ModelMap modelMap, @PathVariable("page") String page) {
        if (StringUtils.isEmpty(page)) {
            return "error";
        }

        page = page.toLowerCase();

        return String.format("page/%s", page);
    }
}
