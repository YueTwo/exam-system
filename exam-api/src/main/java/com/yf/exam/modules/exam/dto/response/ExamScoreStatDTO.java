package com.yf.exam.modules.exam.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* 考试成绩统计
*/
@Data
@ApiModel(value="考试成绩统计", description="考试成绩统计")
public class ExamScoreStatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "考试ID", required = true)
    private String examId;

    @ApiModelProperty(value = "考试名称", required = true)
    private String title;

    @ApiModelProperty(value = "参考人数", required = true)
    private Integer totalUser;

    @ApiModelProperty(value = "及格人数", required = true)
    private Integer passUser;

    @ApiModelProperty(value = "平均分", required = true)
    private BigDecimal avgScore;

    @ApiModelProperty(value = "最高分", required = true)
    private Integer maxScore;

    @ApiModelProperty(value = "最低分", required = true)
    private Integer minScore;

    @ApiModelProperty(value = "通过率(0-100)", required = true)
    private BigDecimal passRate;
}
