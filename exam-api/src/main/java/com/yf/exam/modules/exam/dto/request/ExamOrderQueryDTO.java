package com.yf.exam.modules.exam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "考试订单查询")
public class ExamOrderQueryDTO {

    @ApiModelProperty(value = "考试ID")
    private String examId;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "订单状态：0待支付 1已支付 2已取消")
    private Integer status;
}

