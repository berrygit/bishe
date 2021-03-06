CREATE TABLE `WORKFLOW_INSTANCE` (
  `ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '工作流实例表主键',
  `REQUEST_ID` varchar(64) NOT NULL DEFAULT '' COMMENT '请求ID',
  `WORKFLOW_NAME` varchar(64) NOT NULL COMMENT '工作流实例名称',
  `INIT_INFO` TEXT COMMENT '初始化信息',
  `STATUS` varchar(16) NOT NULL COMMENT '工作流实例当前状态',
  `NODE` varchar(64) DEFAULT NULL COMMENT '当前执行节点信息',
  `TIMEOUT_MILS` bigint(16) DEFAULT NULL COMMENT '超时毫秒数',
  `GMT_BEGIN` datetime DEFAULT NULL COMMENT '实例执行开始时间',
  `GMT_UPDATE` datetime DEFAULT NULL COMMENT '实例执行更新时间',
  `GMT_END` datetime DEFAULT NULL COMMENT '实例执行结束时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WORKFLOW_INSTANCE_BACKUP` (
  `ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '工作流实例备份表主键',
  `REQUEST_ID` varchar(64) NOT NULL DEFAULT '' COMMENT '请求ID',
  `WORKFLOW_NAME` varchar(64) NOT NULL COMMENT '工作流实例名称',
  `INIT_INFO` TEXT COMMENT '初始化信息',
  `STATUS` varchar(16) NOT NULL COMMENT '工作流实例当前状态',
  `NODE` varchar(64) DEFAULT NULL COMMENT '当前执行节点信息',
  `TIMEOUT_MILS` bigint(16) DEFAULT NULL COMMENT '超时毫秒数',
  `GMT_BEGIN` datetime DEFAULT NULL COMMENT '实例执行开始时间',
  `GMT_UPDATE` datetime DEFAULT NULL COMMENT '实例执行更新时间',
  `GMT_END` datetime DEFAULT NULL COMMENT '实例执行结束时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WORKFLOW_TASK` (
	`ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '任务表主键',
	`WORKFLOW_ID` bigint(16) NOT NULL COMMENT '工作流实例ID',
	`TASK_NAME` varchar(64) NOT NULL COMMENT '任务名称',
	`STATUS` varchar(16) NOT NULL COMMENT '工作流任务状态',
	`INPUT` TEXT character set utf8 COMMENT '输入信息',
	`OUTPUT` TEXT character set utf8 COMMENT '输出信息',
	`EXCEPT_MESSAGE` TEXT character set utf8 COMMENT '错误信息',
	`NODE` varchar(64) DEFAULT NULL COMMENT '当前执行节点信息',
	`GMT_BEGIN` datetime DEFAULT NULL COMMENT '任务执行开始时间',
    `GMT_UPDATE` datetime DEFAULT NULL COMMENT '任务执行结束时间',
    PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WORKFLOW_TASK_BACKUP` (
	`ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '任务备份表主键',
	`WORKFLOW_ID` bigint(16) NOT NULL COMMENT '工作流实例ID',
	`TASK_NAME` varchar(64) NOT NULL COMMENT '任务名称',
	`STATUS` varchar(16) NOT NULL COMMENT '工作流任务状态',
	`INPUT` TEXT character set utf8 COMMENT '输入信息',
	`OUTPUT` TEXT character set utf8 COMMENT '输出信息',
	`EXCEPT_MESSAGE` TEXT character set utf8 COMMENT '错误信息',
	`NODE` varchar(64) DEFAULT NULL COMMENT '当前执行节点信息',
	`GMT_BEGIN` datetime DEFAULT NULL COMMENT '任务执行开始时间',
    `GMT_UPDATE` datetime DEFAULT NULL COMMENT '任务执行结束时间',
    PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `HUMAN_OPERATION_LOG` (
	`ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '人工重试表主键',
	`WORKFLOW_ID` bigint(16) NOT NULL COMMENT '工作流ID', 
	`OP_TYPE` varchar(16) NOT NULL COMMENT '操作类型',
	`ADDRESS` varchar(32) NOT NULL COMMENT '操作人所在服务ip信息',
    `GMT_UPDATE` varchar(64) DEFAULT NULL COMMENT '操作执行完毕时间',
    PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `NODE_INFO` (
	`ID` int(10) NOT NULL AUTO_INCREMENT COMMENT '工作流任务执行节点信息表主键',
	`ADDRESS` varchar(32) NOT NULL COMMENT '执行节点ip信息',
	`ROLE` varchar(32) DEFAULT 'Worker' COMMENT '节点角色，Master代表主节点',
	`STATUS` varchar(32) DEFAULT '' COMMENT '节点状态',
	`THROUGHPUT_PER_DAT` bigint(16) DEFAULT 0 COMMENT '每日吞吐率',
	`GMT_BEGIN` datetime DEFAULT NULL COMMENT '信息创建时间',
	`GMT_UPDATE` datetime DEFAULT NULL COMMENT '信息更新时间',
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `CALL_STATISTICS` (
	`ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '调用统计表主键',
	`GMT_TIME` datetime NOT NULL COMMENT '统计获取的时间，以分钟为单位',
	`SUCCESS_COUNT` int(10) DEFAULT 0 COMMENT '成功任务数量',
	`FAILED_COUNT` int(10) DEFAULT 0 COMMENT '失败任务数量',
	`TIMEOUT_COUNT` int(10) DEFAULT 0 COMMENT '超时任务数量',
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `SERVICE_COUNT_STATE` (
	`ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '服务状态表主键',
	`GMT_TIME` datetime NOT NULL COMMENT '统计获取时间，以小时为单位',
	`SERVICE_NAME` varchar(64) NOT NULL COMMENT '服务名称',
	`SUCCESS_COUNT` int(10) DEFAULT 0 COMMENT '成功任务数量',
	`FAILED_COUNT` int(10) DEFAULT 0 COMMENT '失败任务数量',
	`TIMEOUT_COUNT` int(10) DEFAULT 0 COMMENT '超时任务数量',
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `SERVICE_PERFORMANCE_STATE` (
	`ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '服务性能状态表主键',
	`SERVICE_NAME` varchar(64) NOT NULL COMMENT '服务名称',
	`MAX_TIME_COST` bigint(16) DEFAULT 0 COMMENT '最大消耗时间',
	`AVERAGE_TIME_COST` bigint(16) DEFAULT 0 COMMENT '平均消耗时间',
	`MIN_TIME_COST` bigint(16) DEFAULT 0 COMMENT '最小消耗时间',
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;