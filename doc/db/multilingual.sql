CREATE TABLE `multilingual` (
  `id` bigint(64) unsigned NOT NULL,
  `word_key` varchar(255) NOT NULL COMMENT 'key',
  `word_source_value` varchar(255) DEFAULT NULL COMMENT '源语言值',
  `word_target_value` varchar(255) DEFAULT NULL COMMENT '目标语言值',
  `word_target_type` varchar(255) NOT NULL COMMENT '目标语言类型',
  `client_type` int(2) NOT NULL COMMENT '终端类型 1:前端;2:APP-IOS;3:APP-Android;4:UNI-APP;5:后端-异常；6：后端-数据库中数据',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='多语言表';