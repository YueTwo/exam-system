package com.yf.exam.modules.user.exam.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.exam.modules.paper.entity.PaperQu;
import com.yf.exam.modules.user.exam.dto.ExamStatsDTO;
import com.yf.exam.modules.user.exam.dto.UserAnswerDetailDTO;

public interface UserStatsMapper extends BaseMapper<PaperQu>{
    List<ExamStatsDTO> statsByUserAndExam(
            @Param("userId") String userId,
            @Param("examId") String examId
    );

    List<Map<String, Object>> findAnswerDetails(
        @Param("userId") String userId,
        @Param("paperId") String paperId
    );
}
