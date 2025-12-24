package com.yf.exam.modules.exam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "发送通知请求")
public class ExamNoticeSendReqDTO {

    @ApiModelProperty(value = "考试ID", required = true)
    private String examId;

    @ApiModelProperty(value = "通知标题", required = true)
    private String title;

    @ApiModelProperty(value = "通知内容", required = true)
    private String content;
}

