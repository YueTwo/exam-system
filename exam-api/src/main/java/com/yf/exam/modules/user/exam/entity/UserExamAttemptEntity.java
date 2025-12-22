package com.yf.exam.modules.user.exam.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("el_user_exam_attempt")
public class UserExamAttemptEntity {
    @TableId
    private String id;
    private String userId;
    private String examId;
    private Integer score;
    private Boolean passed;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
}
