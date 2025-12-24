package com.yf.exam.modules.exam.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "错题分析")
public class ExamWrongOverviewDTO {

    @ApiModelProperty(value = "题目ID")
    private String quId;

    @ApiModelProperty(value = "题目标题")
    private String title;

    @ApiModelProperty(value = "错误次数（wrong_count求和）")
    private Integer wrongCount;

    @ApiModelProperty(value = "涉及用户数")
    private Integer userCount;
}

