package com.yf.exam.modules.paper.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("el_user_exam_attempt")
public class UserExamAttempt {

    @TableId
    private String id;           // 考试事件ID

    private String userId;       // 用户ID

    private String examId;       // 考试ID

    private Integer attemptNo;   // 第几次考试（可计算）

    private Integer score;       // 本次得分

    private Boolean passed;      // 是否通过

    private Date startTime;      // 开始时间

    private Date endTime;        // 结束时间

    private Date createTime;     // 创建时间
}
