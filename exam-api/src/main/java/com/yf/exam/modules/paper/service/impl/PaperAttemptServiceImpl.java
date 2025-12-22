package com.yf.exam.modules.paper.service.impl;

import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.exam.modules.paper.entity.UserExamAttempt;
import com.yf.exam.modules.paper.mapper.UserPaperMapper;
import com.yf.exam.modules.paper.service.PaperAttemptService;

@Service
public class PaperAttemptServiceImpl
        extends ServiceImpl<UserPaperMapper, UserExamAttempt>
        implements PaperAttemptService {

    /**
     * 创建考试事件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserExamAttempt createAttempt(UserExamAttempt attempt) {
        if (attempt.getId() == null) {
            attempt.setId(String.valueOf(IdWorker.getId()));
        }
        if (attempt.getAttemptNo() == 0) {
            int nextNo = getNextAttemptNo(attempt.getUserId(), attempt.getExamId());
            attempt.setAttemptNo(nextNo);
        }
        attempt.setCreateTime(new Date());
        this.save(attempt);
        return attempt;
    }

    /**
     * 获取最新一次考试事件
     */
    @Override
    public UserExamAttempt getLastAttempt(String userId, String examId) {
        return super.lambdaQuery()
                .eq(UserExamAttempt::getUserId, userId)
                .eq(UserExamAttempt::getExamId, examId)
                .orderByDesc(UserExamAttempt::getAttemptNo)
                .last("LIMIT 1")
                .one();
    }

    /**
     * 获取下一次尝试编号
     */
    @Override
    public int getNextAttemptNo(String userId, String examId) {
        UserExamAttempt last = getLastAttempt(userId, examId);
        return last != null ? last.getAttemptNo() + 1 : 1;
    }

}
