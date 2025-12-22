package com.yf.exam.modules.user.exam.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.modules.user.exam.dto.ExamStatsDTO;
import com.yf.exam.modules.user.exam.dto.request.UserExamReqDTO;
import com.yf.exam.modules.user.exam.dto.request.UserStatsReqDTO;
import com.yf.exam.modules.user.exam.dto.response.UserExamRespDTO;
import com.yf.exam.modules.user.exam.entity.UserExam;
import com.yf.exam.modules.user.exam.mapper.UserExamAnswerMapper;
import com.yf.exam.modules.user.exam.mapper.UserExamMapper;
import com.yf.exam.modules.user.exam.service.UserStatsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStatsServiceImpl extends ServiceImpl<UserExamMapper, UserExam> implements UserStatsService{

    private final UserExamAnswerMapper statsMapper;

    @Override
    public IPage<UserExamRespDTO> itemlist(PagingReqDTO<UserExamReqDTO> reqDTO) {
        UserExamReqDTO params = reqDTO.getParams();
        if (params == null) {
            params = new UserExamReqDTO();
        }
        IPage<UserExamRespDTO> pageData = baseMapper.paging(reqDTO.toPage(), params);
        return pageData;
    }

    @Override
    public List<ExamStatsDTO> common(UserStatsReqDTO reqDTO){
        String userId = reqDTO.getUserId();
        String examId = reqDTO.getExamId();

        // 1. 调用 Mapper 获取题型统计
        List<ExamStatsDTO> statsList = statsMapper.statsByUserAndExam(userId, examId);

        return statsList;
    }
}
