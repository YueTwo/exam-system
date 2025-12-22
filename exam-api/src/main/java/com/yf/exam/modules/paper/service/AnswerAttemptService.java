package com.yf.exam.modules.paper.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.exam.modules.paper.entity.UserAnswerAttempt;

public interface AnswerAttemptService extends IService<UserAnswerAttempt>{
    /**
     * 批量保存作答记录
     */
    void saveBatchAnswers(List<UserAnswerAttempt> answers);
}
