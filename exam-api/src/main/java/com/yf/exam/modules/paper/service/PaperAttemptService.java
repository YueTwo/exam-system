package com.yf.exam.modules.paper.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.exam.modules.paper.entity.UserAnswerAttempt;
import com.yf.exam.modules.paper.entity.UserExamAttempt;

public interface PaperAttemptService extends IService<UserExamAttempt> {

    /**
     * 创建考试事件
     * @param attempt 实体对象
     * @return 创建成功的实体
     */
    UserExamAttempt createAttempt(UserExamAttempt attempt);

    /**
     * 根据用户ID和考试ID查询最新一次考试事件
     */
    UserExamAttempt getLastAttempt(String userId, String examId);

    int getNextAttemptNo(String userId, String examId);
}