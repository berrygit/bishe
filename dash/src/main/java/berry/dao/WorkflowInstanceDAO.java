package berry.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import berry.model.CallStaticsInfo;
import berry.model.NodeStateCount;
import berry.model.ServiceStateCost;
import berry.model.ServiceStateCount;
import berry.model.WorkflowInstanceBean;

public interface WorkflowInstanceDAO {
	
	@Results({
            @Result(property = "count", column = "count"),
            @Result(property = "status", column = "status"),
			})
	@Select("select status, count(*) count from workflow_instance where gmt_end > date_sub(now(), interval '6' minute)"
			+ " and date_format(gmt_end, '%Y-%m-%d %H:%i') = date_format(date_sub(now(), interval '5' minute),"
			+ "'%Y-%m-%d %H:%i') group by status")
	List<CallStaticsInfo> getCallStaticsInfoByMinite();
	
	@Results({
        @Result(property = "count", column = "count"),
        @Result(property = "status", column = "status"),
		})
	@Select("select count(*) from workflow_instance where status = 'schedule'")
	long getScheduleTaskCount();
	
	@Results({
        @Result(property = "count", column = "count"),
        @Result(property = "status", column = "status"),
        @Result(property = "workflowName", column = "workflow_name"),
		})
	@Select("select workflow_name, status, count(*) count from workflow_instance where date_format(gmt_end, '%Y-%m-%d %H') "
		+ "= date_format(date_sub(now(), interval '1' hour),'%Y-%m-%d %H') group by workflow_name, status")
	List<ServiceStateCount> getServiceStateCountInfo();
	
	@Results({
        @Result(property = "max", column = "max"),
        @Result(property = "min", column = "min"),
        @Result(property = "average", column = "average"),
        @Result(property = "workflowName", column = "workflow_name"),
		})
	@Select("select max(TIMESTAMPDIFF(second, gmt_begin, gmt_end)) max, min(TIMESTAMPDIFF(second, gmt_begin, gmt_end))"
			+ " min, avg(TIMESTAMPDIFF(second, gmt_begin, gmt_end)) average, workflow_name from workflow_instance where"
			+ "date_format(gmt_end, '%Y-%m-%d %H') = date_format(date_sub(now(), interval '1' hour),'%Y-%m-%d %H')"
			+ "and status = 'finish' group by workflow_name")
	List<ServiceStateCost> getServiceStateCostInfo();

	@Select("<script>"
			+ "select * from workflow_instance where <when test='title!=null'> AND ID = #{id}</when>"
			+ "<when test='title!=null'> AND REQUEST_ID = #{requestId}</when>"
			+ "<when test='title!=null'> AND WORKFLOW_NAME = #{workflowName}</when>"
			+ "<when test='title!=null'> AND STATUS = #{status}</when>"
			+ "<when test='title!=null'> AND NODE = #{node}</when>"
			+ "<when test='title!=null'> AND GMT_BEGIN = #{begin}</when>"
			+ "<when test='title!=null'> AND GMT_END = #{end}</when>"
			+ " limit 100 </script>")
	List<WorkflowInstanceBean> queryWithLimit(@Param("id")String id, @Param("requestId")String requestId,
			@Param("workflowName")String workflowName,	@Param("status")String status, @Param("node")String node, 
			@Param("begin")Date begin, @Param("end")Date end);

	void updateStatus(String status,String id);
	
	@Results({
        @Result(property = "count", column = "count"),
        @Result(property = "node", column = "node"),
		})
	@Select("select node, count(*) count from workflow_instance where date_format(gmt_end, '%Y-%m-%d') "
		+ "= date_format(date_sub(now(), interval '1' day),'%Y-%m-%d') and status= 'finish' group by node")
	List<NodeStateCount> getNodeStateCountInfo();

}
