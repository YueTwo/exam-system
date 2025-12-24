package com.yf.exam.modules.exam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "考试分享链接解析请求")
public class ExamShareResolveReqDTO {

    @ApiModelProperty(value = "分享Token", required = true)
    private String shareToken;
}

