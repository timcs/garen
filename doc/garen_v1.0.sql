create TABLE `group` (
  `id` int(11) NOT NULL COMMENT '主键',
  `group_name` varchar(64) NOT NULL DEFAULT '' COMMENT '应用名称',
  `group_desc` varchar(256) NOT NULL DEFAULT '' COMMENT '应用名称描述',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0无效，1有效',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0未删除,1删除',
  `creator` varchar(64) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_groupName` (`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用信息表';


create TABLE `job` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` int(11) NOT NULL COMMENT '应用名称',
  `job_name` varchar(128) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_param` varchar(1024) NOT NULL DEFAULT '' COMMENT '任务参数',
  `job_desc` varchar(512) NOT NULL DEFAULT '' COMMENT '任务描述',
  `job_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '任务类型',
  `protocol_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '协议类型',
  `cron` varchar(64) NOT NULL DEFAULT '' COMMENT 'cron表达式',
  `url` varchar(256) NOT NULL DEFAULT '' COMMENT '执行路径',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0禁用，1启用，3暂停',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '0未删除,1删除',
  `creator` varchar(64) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create TABLE `job_execute_detail_log` (
  `id` bigint(20) NOT NULL COMMENT '子任务ID',
  `execute_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '主任务ID',
  `url` varchar(256) NOT NULL DEFAULT '' COMMENT '主机名称/IP',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始执行时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束执行时间',
  `resp_code` varchar(8) DEFAULT '' COMMENT '响应码',
  `resp_msg` varchar(128) DEFAULT '' COMMENT '响应消息',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行状态',
  `memo` varchar(4096) DEFAULT '' COMMENT '备注,描述',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '0未删除,1删除',
  `creator` varchar(64) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idxExecuteId` (`execute_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务执行日志详细表';


create TABLE `job_execute_log` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `job_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '任务id',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始执行时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束执行时间',
  `next_time` timestamp NULL DEFAULT NULL COMMENT '下次执行时间',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行状态',
  `memo` varchar(4096) DEFAULT '' COMMENT '备注,描述',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '0未删除,1删除',
  `creator` varchar(64) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idxJobId` (`job_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务执行日志表';

create TABLE `log` (
  `id` bigint(20) NOT NULL  COMMENT '主键',
  `execute_no` bigint(20) NOT NULL DEFAULT '0' COMMENT '执行编号',
  `job_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '任务id',
   `request_url` varchar(256) NOT NULL DEFAULT '' COMMENT '执行路径',
   `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始执行时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束执行时间',
  `next_time` timestamp NULL DEFAULT NULL COMMENT '下次执行时间',
  `resp_code` varchar(8) DEFAULT '' COMMENT '响应码',
  `resp_msg` varchar(128) DEFAULT '' COMMENT '响应消息',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行状态',
  `memo` varchar(512) DEFAULT '' COMMENT '备注,描述',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '0未删除,1删除',
  `creator` varchar(64) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(64) NOT NULL DEFAULT '' COMMENT '修改人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idxJobId` (`job_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务执行日志表';

