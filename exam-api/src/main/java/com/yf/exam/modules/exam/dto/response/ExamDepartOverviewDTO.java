package com.yf.exam.modules.exam.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "部门概览")
public class ExamDepartOverviewDTO {

    @ApiModelProperty(value = "部门ID")
    private String departId;

    @ApiModelProperty(value = "部门名称")
    private String departName;

    @ApiModelProperty(value = "部门用户数")
    private Integer userCount;

    @ApiModelProperty(value = "参与考试用户数")
    private Integer examUserCount;

    @ApiModelProperty(value = "通过记录数")
    private Integer passCount;

    @ApiModelProperty(value = "平均最高分")
    private BigDecimal avgScore;
}

