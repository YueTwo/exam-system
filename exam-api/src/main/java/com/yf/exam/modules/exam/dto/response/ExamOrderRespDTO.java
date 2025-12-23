package com.yf.exam.modules.exam.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "考试订单响应")
public class ExamOrderRespDTO {

    @ApiModelProperty(value = "订单ID")
    private String id;

    @ApiModelProperty(value = "考试ID")
    private String examId;

    @ApiModelProperty(value = "考试名称")
    private String examTitle;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "金额（分）")
    private Integer amountCent;

    @ApiModelProperty(value = "状态：0待支付 1已支付 2已取消")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "支付时间")
    private Date payTime;
}

