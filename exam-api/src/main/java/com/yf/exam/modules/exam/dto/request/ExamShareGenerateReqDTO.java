package com.yf.exam.modules.exam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "考试分享链接生成请求")
public class ExamShareGenerateReqDTO {

    @ApiModelProperty(value = "考试ID", required = true)
    private String examId;

    @ApiModelProperty(value = "过期时间（可选）")
    private Date expireTime;
}

