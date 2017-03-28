CREATE TABLE `WORKFLOW_INSTANCE` (
  `ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '工作流实例表主键',
  `REQUEST_ID` varchar(64) NOT NULL DEFAULT '' COMMENT '请求ID',
  `WORKFLOW_NAME` varchar(64) NOT NULL COMMENT '工作流实例名称',
  `INIT_INFO` TEXT COMMENT '初始化信息',
  `STATUS` varchar(16) NOT NULL COMMENT '工作流实例当前状态',
  `TIMEOUT_MILS` bigint(16) DEFAULT NULL COMMENT '超时毫秒数',
  `CURRENT_NODE` varchar(64) DEFAULT NULL COMMENT '当前工作流实例的执行节点', 
  `CURRENT_STEP` varchar(64) DEFAULT NULL COMMENT '当前工作流实例执行到的步骤',
  `GMT_BEGIN` varchar(64) DEFAULT NULL COMMENT '实例执行开始时间',
  `GMT_END` varchar(64) DEFAULT NULL COMMENT '实例执行结束时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `WORKFLOW_INSTANCE_BACKUP` (
  `ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '工作流实例备份表主键',
  `REQUEST_ID` varchar(64) NOT NULL DEFAULT '' COMMENT '请求ID',
  `WORKFLOW_NAME` varchar(64) NOT NULL COMMENT '工作流实例名称',
  `INIT_INFO` TEXT COMMENT '初始化信息',
  `STATUS` varchar(16) NOT NULL COMMENT '工作流实例当前状态',
  `TIMEOUT_MILS` bigint(16) DEFAULT NULL COMMENT '超时毫秒数',
  `CURRENT_NODE` varchar(64) DEFAULT NULL COMMENT '当前工作流实例的执行节点', 
  `CURRENT_STEP` varchar(64) DEFAULT NULL COMMENT '当前工作流实例执行到的步骤',
  `GMT_BEGIN` varchar(64) DEFAULT NULL COMMENT '实例执行开始时间',
  `GMT_END` varchar(64) DEFAULT NULL COMMENT '实例执行结束时间',
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
	`CURRENT_NODE` varchar(64) DEFAULT NULL COMMENT '当前执行节点信息',
	`GMT_BEGIN` varchar(64) DEFAULT NULL COMMENT '任务执行开始时间',
    `GMT_END` varchar(64) DEFAULT NULL COMMENT '任务执行结束时间',
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
	`CURRENT_NODE` varchar(64) DEFAULT NULL COMMENT '当前执行节点信息',
	`GMT_BEGIN` varchar(64) DEFAULT NULL COMMENT '任务执行开始时间',
    `GMT_END` varchar(64) DEFAULT NULL COMMENT '任务执行结束时间',
    PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `HUMAN_RETRY_LOG` (
	`ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '人工重试表主键',
	`WORKFLOW_ID` bigint(16) NOT NULL COMMENT '工作流ID', 
	`STATUS` varchar(16) NOT NULL COMMENT '操作状态',
	`ADDRESS` varchar(32) NOT NULL COMMENT '操作人所在服务ip信息',
	`GMT_BEGIN` varchar(64) DEFAULT NULL COMMENT '操作触发时间',
    `GMT_END` varchar(64) DEFAULT NULL COMMENT '操作执行完毕时间',
    PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `NODE_INFO` (
	`ID` int(10) NOT NULL AUTO_INCREMENT COMMENT '工作流任务执行节点信息表主键',
	`ADDRESS` varchar(32) NOT NULL COMMENT '执行节点ip信息',
	`GMT_BEGIN` varchar(64) DEFAULT NULL COMMENT '信息创建时间',
	`GMT_UPDATE` varchar(64) DEFAULT NULL COMMENT '信息更新时间',
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
