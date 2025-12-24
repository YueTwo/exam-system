package com.yf.exam.modules.exam.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "考试分享链接响应")
public class ExamShareRespDTO {

    @ApiModelProperty(value = "考试ID", required = true)
    private String examId;

    @ApiModelProperty(value = "是否启用分享", required = true)
    private Boolean shareEnabled;

    @ApiModelProperty(value = "分享Token")
    private String shareToken;

    @ApiModelProperty(value = "过期时间")
    private Date shareExpireTime;
}

