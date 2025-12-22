package com.yf.exam.modules.user.exam.dto;

import lombok.Data;

@Data
public class ExamStatsDTO {
    private Integer quType;    // 题型
    private Integer total;     // 总题数
    private Integer rightCount; // 做对题数
}
