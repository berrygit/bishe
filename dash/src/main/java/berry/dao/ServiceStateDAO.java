package berry.dao;

import berry.model.ServiceStateBean;

public interface ServiceStateDAO {

	void insert(ServiceStateBean serviceStateBean);

	void getStateInfoOneDay();

	void getStateInfoOneDayByService(String serviceName);

}
