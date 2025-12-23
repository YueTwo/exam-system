package com.yf.exam.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.exam.modules.exam.dto.request.ExamOrderQueryDTO;
import com.yf.exam.modules.exam.dto.response.ExamOrderRespDTO;
import com.yf.exam.modules.exam.entity.ExamOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ExamOrderMapper extends BaseMapper<ExamOrder> {

    @Select({
            "<script>",
            "SELECT",
            "  o.id,",
            "  o.exam_id AS examId,",
            "  e.title AS examTitle,",
            "  o.user_id AS userId,",
            "  u.user_name AS userName,",
            "  o.amount_cent AS amountCent,",
            "  o.status,",
            "  o.create_time AS createTime,",
            "  o.pay_time AS payTime",
            "FROM el_exam_order o",
            "LEFT JOIN el_exam e ON e.id=o.exam_id",
            "LEFT JOIN sys_user u ON u.id=o.user_id",
            "<where>",
            "  <if test='query!=null'>",
            "    <if test='query.examId!=null and query.examId!=\"\"'> AND o.exam_id=#{query.examId} </if>",
            "    <if test='query.userId!=null and query.userId!=\"\"'> AND o.user_id=#{query.userId} </if>",
            "    <if test='query.status!=null'> AND o.status=#{query.status} </if>",
            "  </if>",
            "</where>",
            "ORDER BY o.create_time DESC",
            "</script>"
    })
    IPage<ExamOrderRespDTO> paging(Page page, @Param("query") ExamOrderQueryDTO query);
}

