/*
  Exam-system upgrade script
  Adds: exam settings / share / pay / notices
  Tested for MySQL 8.x
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for el_exam_setting
-- ----------------------------
CREATE TABLE IF NOT EXISTS `el_exam_setting` (
  `exam_id` varchar(64) NOT NULL COMMENT '考试ID',
  `allow_late` tinyint NOT NULL DEFAULT 1 COMMENT '允许迟到',
  `exam_notice` varchar(2000) NOT NULL DEFAULT '' COMMENT '考前注意事项',
  `result_show_type` int NOT NULL DEFAULT 2 COMMENT '1仅感谢文字；2感谢文字+成绩',
  `thank_text` varchar(1000) NOT NULL DEFAULT '感谢参加考试！' COMMENT '感谢文字',
  `max_try_count` int NOT NULL DEFAULT 0 COMMENT '限考次数(0不限)',
  `reward_points` int NOT NULL DEFAULT 0 COMMENT '赠送积分',
  `min_submit_minutes` int NOT NULL DEFAULT 0 COMMENT '最低交卷时长(分钟)',
  `price_cent` int NOT NULL DEFAULT 0 COMMENT '售价(分)',
  `share_enabled` tinyint NOT NULL DEFAULT 0 COMMENT '是否启用分享',
  `share_token` varchar(64) DEFAULT NULL COMMENT '分享Token',
  `share_expire_time` datetime DEFAULT NULL COMMENT '分享过期时间',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`exam_id`),
  UNIQUE KEY `uk_share_token` (`share_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试扩展配置';

-- ----------------------------
-- Table structure for el_exam_order
-- ----------------------------
CREATE TABLE IF NOT EXISTS `el_exam_order` (
  `id` varchar(64) NOT NULL COMMENT '订单ID',
  `exam_id` varchar(64) NOT NULL COMMENT '考试ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `amount_cent` int NOT NULL DEFAULT 0 COMMENT '金额(分)',
  `status` int NOT NULL DEFAULT 0 COMMENT '0待支付 1已支付 2已取消',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `pay_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exam_user` (`exam_id`,`user_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试订单';

-- ----------------------------
-- Table structure for el_exam_notice
-- ----------------------------
CREATE TABLE IF NOT EXISTS `el_exam_notice` (
  `id` varchar(64) NOT NULL COMMENT '通知ID',
  `exam_id` varchar(64) NOT NULL COMMENT '考试ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `create_user_id` varchar(64) NOT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_exam` (`exam_id`),
  KEY `idx_create_user` (`create_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试通知';

-- ----------------------------
-- Init default settings for existing exams
-- ----------------------------
INSERT INTO el_exam_setting (exam_id)
SELECT e.id
FROM el_exam e
WHERE NOT EXISTS (SELECT 1 FROM el_exam_setting s WHERE s.exam_id = e.id);

SET FOREIGN_KEY_CHECKS = 1;

