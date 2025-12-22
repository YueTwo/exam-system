CREATE TABLE el_user_exam_attempt (
  id           varchar(32)  NOT NULL COMMENT '考试事件ID',
  user_id      varchar(32)  NOT NULL COMMENT '用户ID',
  exam_id      varchar(32)  NOT NULL COMMENT '考试ID',
  attempt_no   int          NOT NULL,
  score        int          NOT NULL DEFAULT 0 COMMENT '本次得分',
  passed       tinyint      NOT NULL DEFAULT 0 COMMENT '是否通过',
  start_time   datetime     NOT NULL COMMENT '开始时间',
  end_time     datetime     DEFAULT NULL COMMENT '结束时间',
  create_time  datetime     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_exam_attempt (user_id, exam_id, attempt_no),
  KEY idx_user_exam (user_id, exam_id)
) COMMENT='用户考试事件表'
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;

CREATE TABLE el_user_exam_answer (
  id            varchar(32)  NOT NULL COMMENT '作答记录ID',
  attempt_id    varchar(32)  NOT NULL COMMENT '考试事件ID',
  qu_id         varchar(64)  NOT NULL COMMENT '题目ID',
  user_answer   varchar(1000) NOT NULL COMMENT '用户答案',
  is_right      tinyint      NOT NULL COMMENT '是否正确',
  score         int          NOT NULL DEFAULT 0 COMMENT '本题得分',
  create_time   datetime     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_attempt_qu (attempt_id, qu_id),
  KEY idx_attempt (attempt_id),
  KEY idx_qu (qu_id)
) COMMENT='用户逐题作答记录'
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;