package com.yf.exam.modules.user.exam.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.modules.user.exam.dto.ExamStatsDTO;
import com.yf.exam.modules.user.exam.dto.UserAnswerDetailDTO;
import com.yf.exam.modules.user.exam.dto.request.UserExamReqDTO;
import com.yf.exam.modules.user.exam.dto.request.UserPaperReqDTO;
import com.yf.exam.modules.user.exam.dto.request.UserStatsReqDTO;
import com.yf.exam.modules.user.exam.dto.response.UserExamRespDTO;
import com.yf.exam.modules.user.exam.entity.UserExam;

public interface UserStatsService extends IService<UserExam>{
    IPage<UserExamRespDTO> itemlist(PagingReqDTO<UserExamReqDTO> reqDTO);

    List<ExamStatsDTO> common(UserStatsReqDTO reqDTO);

    List<UserAnswerDetailDTO> result(UserPaperReqDTO reqDTO);
}