package com.yf.exam.modules.exam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "订单标记已支付请求")
public class ExamOrderMarkPaidReqDTO {

    @ApiModelProperty(value = "订单ID", required = true)
    private String orderId;
}

