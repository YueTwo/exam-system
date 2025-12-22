package com.yf.exam.modules.paper.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("el_user_exam_answer")
public class UserAnswerAttempt {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;           // 作答记录ID

    private String attemptId;    // 考试事件ID

    private String quId;         // 题目ID

    private String userAnswer;   // 用户答案

    private Integer isRight;     // 是否正确

    private Integer score;       // 本题得分

    private Date createTime;     // 创建时间
}
