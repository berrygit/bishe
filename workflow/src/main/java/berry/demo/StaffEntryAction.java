package berry.demo;

import java.util.Map;

import berry.api.WorkflowContext;

public class StaffEntryAction {

	public Map<String, Object> checkPersonalInfo(WorkflowContext context) {
		System.out.println("checkPersonalInfo");
		return null;
	}
	public Map<String, Object> signContract(WorkflowContext context) {
		System.out.println("signContract");
		return null;
	}
	public Map<String, Object> train(WorkflowContext context) {
		System.out.println("train");
		return null;
	}
	public Map<String, Object> clearStaffInfo(WorkflowContext context) {
		System.out.println("clearStaffInfo");
		return null;
	}
}
