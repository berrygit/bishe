package berry.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import berry.dao.ServiceStateDAO;
import berry.dao.WorkflowInstanceDAO;
import berry.enums.WorkflowInstanceState;
import berry.model.ServiceStateBean;
import berry.model.ServiceStateCost;
import berry.model.ServiceStateCount;

public class ServiceStateJob {

	@Resource
	private WorkflowInstanceDAO workflowInstanceDAO;
	
	@Resource
	private ServiceStateDAO serviceStateDAO;

	@Scheduled(cron = "0 10 0-23 * * ? ")
	public void loadServiceStateInfo() {
		
		Map<String, ServiceStateBean> serviceBeanMap = new HashMap<String, ServiceStateBean>();
		
		List<ServiceStateCount> serviceStates = workflowInstanceDAO.getServiceStateCountInfo();
		
		for (ServiceStateCount serviceState : serviceStates) {
			
			ServiceStateBean serviceStateBean = null;
			
			if (serviceBeanMap.get(serviceState.getWorkflowName()) == null){
				serviceStateBean = new ServiceStateBean();
				serviceStateBean.setServiceName(serviceState.getWorkflowName());
				serviceBeanMap.put(serviceState.getWorkflowName(), serviceStateBean);
			}else{
				serviceStateBean = serviceBeanMap.get(serviceState.getWorkflowName());
			}
			if (WorkflowInstanceState.FINISH.name().equals(serviceState.getStatus())) {
				serviceStateBean.setSuccessCount(serviceState.getCount());
			}
			if (WorkflowInstanceState.FAILED.name().equals(serviceState.getStatus())) {
				serviceStateBean.setFailedCount(serviceState.getCount());
			}
			if (WorkflowInstanceState.TIMEOUT.name().equals(serviceState.getStatus())) {
				serviceStateBean.setTimeoutCount(serviceState.getCount());
			}
		}
		
		List<ServiceStateCost> costInfos = workflowInstanceDAO.getServiceStateCostInfo();
		
		for (ServiceStateCost costInfo : costInfos) {
			
			ServiceStateBean serviceStateBean = null;
			
			if (serviceBeanMap.get(costInfo.getWorkflowName()) == null){
				serviceStateBean = new ServiceStateBean();
				serviceStateBean.setServiceName(costInfo.getWorkflowName());
				serviceBeanMap.put(costInfo.getWorkflowName(), serviceStateBean);
			}else{
				serviceStateBean = serviceBeanMap.get(costInfo.getWorkflowName());
			}
			
			serviceStateBean.setMax(costInfo.getMax());
			serviceStateBean.setMin(costInfo.getMin());
			serviceStateBean.setAverage(costInfo.getAverage());
			
		}
		
		for (ServiceStateBean serviceStateBean : serviceBeanMap.values()){
			serviceStateDAO.insert(serviceStateBean);
		}
		
	}
}
