package com.yf.exam.modules.exam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "考试订单创建请求")
public class ExamOrderCreateReqDTO {

    @ApiModelProperty(value = "考试ID", required = true)
    private String examId;
}

