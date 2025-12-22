package com.yf.exam.modules.user.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.exam.modules.user.exam.dto.ExamStatsDTO;
import com.yf.exam.modules.user.exam.entity.UserExamAnswerEntity;

@Mapper
public interface UserExamAnswerMapper extends BaseMapper<UserExamAnswerEntity> {

    /**
     * 统计用户在某考试（可能多次 attempt）各题型的做对数量和总题数
     */
    String sql_str = "SELECT q.qu_type AS quType, "
           + "COUNT(a.id) AS total, "
           + "SUM(CASE WHEN a.is_right = 1 THEN 1 ELSE 0 END) AS rightCount "
           + "FROM el_user_exam_answer a "
           + "LEFT JOIN el_user_exam_attempt t ON a.attempt_id = t.id "
           + "LEFT JOIN el_qu q ON a.qu_id = q.id "
           + "WHERE t.user_id = #{userId} "
           + "AND t.exam_id = #{examId} "
           + "GROUP BY q.qu_type";
    @Select(sql_str)
    List<ExamStatsDTO> statsByUserAndExam(
      @Param("userId") String userId, @Param("examId") String examId
    );
}
