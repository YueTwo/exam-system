package com.yf.exam.modules.paper.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.exam.modules.paper.entity.UserAnswerAttempt;
import com.yf.exam.modules.paper.mapper.UserAnswerMapper;
import com.yf.exam.modules.paper.service.AnswerAttemptService;

@Service
public class AnswerAttemptServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswerAttempt>
        implements AnswerAttemptService{
    /**
     * 批量保存作答记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatchAnswers(List<UserAnswerAttempt> answers) {
        if (answers == null || answers.isEmpty()) {
            return;
        }
        for (UserAnswerAttempt ans : answers) {
            if (ans.getId() == null) {
                ans.setId(String.valueOf(IdWorker.getId()));
            }
            if (ans.getCreateTime() == null) {
                ans.setCreateTime(new Date());
            }
        }
        this.saveBatch(answers);
    }
}
