create TABLE `job_config` (
  `id` bigint NOT NULL COMMENT '主键',
  `job_no` BIGINT NOT NULL COMMENT '任务编号'
  `job_name` varchar(128) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_param` varchar(1024) NOT NULL DEFAULT '' COMMENT '任务参数',
  `job_desc` varchar(512) NOT NULL DEFAULT '' COMMENT '任务描述',
  `job_type` tinyint NOT NULL DEFAULT '0' COMMENT '任务类型',
  `protocol_type` tinyint NOT NULL DEFAULT '0' COMMENT '协议类型',
  `cron` varchar(64) NOT NULL DEFAULT '' COMMENT 'cron表达式',
  `target_address` varchar(256) NOT NULL DEFAULT '' COMMENT '执行路径',
  `executor_ip` varchar(64) NOT NULL DEFAULT '' COMMENT '执行器ip',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0禁用，1启用，3暂停',
  `deleted` tinyint DEFAULT '0' COMMENT '0未删除,1删除',
  `creator` varchar(64) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4 COMMENT='任务配置表';

create TABLE `job_execute_log` (
  `id` bigint NOT NULL  COMMENT '主键',
  `execute_no` bigint NOT NULL DEFAULT '0' COMMENT '执行编号',
  `job_id` bigint NOT NULL DEFAULT '0' COMMENT '任务id',
  `executor_ip` varchar(64) NOT NULL DEFAULT '' COMMENT '执行器',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始执行时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束执行时间',
  `next_time` timestamp NULL DEFAULT NULL COMMENT '下次执行时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '执行状态',
  `memo` varchar(2048) DEFAULT '' COMMENT '备注,描述',
  `deleted` tinyint DEFAULT '0' COMMENT '0未删除,1删除',
  `creator` varchar(64) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idxJobId` (`job_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4 COMMENT='任务执行日志表';

create TABLE `job_execute_detail_log` (
  `id` bigint NOT NULL COMMENT '子任务ID',
  `job_execute_log_id` bigint NOT NULL DEFAULT '0' COMMENT '主任务ID',
  `target_address` varchar(256) NOT NULL DEFAULT '' COMMENT '主机名称/IP',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始执行时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束执行时间',
  `request_body` varchar(512) DEFAULT '' COMMENT '请求体',
  `response_body` varchar(512) DEFAULT '' COMMENT '响应体',
  `memo` varchar(2048) DEFAULT '' COMMENT '备注,描述',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '执行状态',
  `deleted` tinyint DEFAULT '0' COMMENT '0未删除,1删除',
  `creator` varchar(64) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idxJobExecuteLogId` (`job_execute_log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4 COMMENT='任务执行日志详细表';

