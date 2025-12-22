package com.yf.exam.modules.user.exam.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("el_user_exam_answer")
public class UserExamAnswerEntity {
    @TableId
    private String id;
    private String attemptId;
    private String quId;
    private String userAnswer;
    private Boolean isRight;
    private Integer score;
    private LocalDateTime createTime;
}
