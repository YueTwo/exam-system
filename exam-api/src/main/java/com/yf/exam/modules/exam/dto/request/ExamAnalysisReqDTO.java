package com.yf.exam.modules.exam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "统计分析请求")
public class ExamAnalysisReqDTO {

    @ApiModelProperty(value = "考试ID（可选，不填表示全局统计）")
    private String examId;

    @ApiModelProperty(value = "返回条数（默认20）")
    private Integer top;
}

