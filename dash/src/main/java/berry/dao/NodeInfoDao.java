package berry.dao;

import java.util.List;

import berry.model.NodeInfoBean;

public interface NodeInfoDao {

	void insert(NodeInfoBean nodeInfoBean);
	void update(NodeInfoBean nodeInfoBean);
	void delete(NodeInfoBean nodeInfoBean);
	
	List<NodeInfoBean> query();

}
