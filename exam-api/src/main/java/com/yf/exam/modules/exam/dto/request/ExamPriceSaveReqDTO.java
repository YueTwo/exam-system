package com.yf.exam.modules.exam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "考试售价配置请求")
public class ExamPriceSaveReqDTO {

    @ApiModelProperty(value = "考试ID", required = true)
    private String examId;

    @ApiModelProperty(value = "售价（分），0表示免费", required = true)
    private Integer priceCent;
}

