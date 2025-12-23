package com.yf.exam.modules.exam.mapper;

import com.yf.exam.modules.exam.dto.response.ExamDepartOverviewDTO;
import com.yf.exam.modules.exam.dto.response.ExamPersonOverviewDTO;
import com.yf.exam.modules.exam.dto.response.ExamWrongOverviewDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExamAnalysisMapper {

    @Select({
            "<script>",
            "SELECT",
            "  u.id AS userId,",
            "  u.user_name AS userName,",
            "  u.real_name AS realName,",
            "  u.depart_id AS departId,",
            "  d.faculty_name AS departName,",
            "  COALESCE(COUNT(ue.id),0) AS examCount,",
            "  COALESCE(SUM(CASE WHEN ue.passed=1 THEN 1 ELSE 0 END),0) AS passCount,",
            "  COALESCE(AVG(ue.max_score),0) AS avgScore,",
            "  COALESCE(MAX(ue.max_score),0) AS maxScore,",
            "  COALESCE(SUM(ue.try_count),0) AS totalTryCount",
            "FROM sys_user u",
            "LEFT JOIN sys_depart d ON d.id=u.depart_id",
            "LEFT JOIN el_user_exam ue ON ue.user_id=u.id",
            "<where>",
            "  <if test='examId!=null and examId!=\"\"'>",
            "    AND ue.exam_id = #{examId}",
            "  </if>",
            "</where>",
            "GROUP BY u.id, u.user_name, u.real_name, u.depart_id, d.faculty_name",
            "ORDER BY examCount DESC, passCount DESC",
            "LIMIT #{top}",
            "</script>"
    })
    List<ExamPersonOverviewDTO> personOverview(@Param("examId") String examId, @Param("top") Integer top);

    @Select({
            "<script>",
            "SELECT",
            "  d.id AS departId,",
            "  d.faculty_name AS departName,",
            "  COALESCE(COUNT(DISTINCT u.id),0) AS userCount,",
            "  COALESCE(COUNT(DISTINCT ue.user_id),0) AS examUserCount,",
            "  COALESCE(SUM(CASE WHEN ue.passed=1 THEN 1 ELSE 0 END),0) AS passCount,",
            "  COALESCE(AVG(ue.max_score),0) AS avgScore",
            "FROM sys_depart d",
            "LEFT JOIN sys_user u ON u.depart_id=d.id",
            "LEFT JOIN el_user_exam ue ON ue.user_id=u.id",
            "<where>",
            "  <if test='examId!=null and examId!=\"\"'>",
            "    AND ue.exam_id = #{examId}",
            "  </if>",
            "</where>",
            "GROUP BY d.id, d.faculty_name",
            "ORDER BY userCount DESC, examUserCount DESC",
            "LIMIT #{top}",
            "</script>"
    })
    List<ExamDepartOverviewDTO> departOverview(@Param("examId") String examId, @Param("top") Integer top);

    @Select({
            "<script>",
            "SELECT",
            "  ub.qu_id AS quId,",
            "  ub.title AS title,",
            "  COALESCE(SUM(ub.wrong_count),0) AS wrongCount,",
            "  COALESCE(COUNT(DISTINCT ub.user_id),0) AS userCount",
            "FROM el_user_book ub",
            "<where>",
            "  <if test='examId!=null and examId!=\"\"'>",
            "    AND ub.exam_id = #{examId}",
            "  </if>",
            "</where>",
            "GROUP BY ub.qu_id, ub.title",
            "ORDER BY wrongCount DESC",
            "LIMIT #{top}",
            "</script>"
    })
    List<ExamWrongOverviewDTO> wrongOverview(@Param("examId") String examId, @Param("top") Integer top);
}

