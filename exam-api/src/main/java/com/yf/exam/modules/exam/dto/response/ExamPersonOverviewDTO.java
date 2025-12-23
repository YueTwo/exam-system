package com.yf.exam.modules.exam.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "人员概览")
public class ExamPersonOverviewDTO {

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "部门ID")
    private String departId;

    @ApiModelProperty(value = "部门名称")
    private String departName;

    @ApiModelProperty(value = "参与考试数")
    private Integer examCount;

    @ApiModelProperty(value = "通过次数")
    private Integer passCount;

    @ApiModelProperty(value = "平均最高分")
    private BigDecimal avgScore;

    @ApiModelProperty(value = "最高分")
    private Integer maxScore;

    @ApiModelProperty(value = "累计考试次数（try_count求和）")
    private Integer totalTryCount;
}

