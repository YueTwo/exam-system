package com.yf.exam.modules.exam.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "通知查询")
public class ExamNoticeQueryDTO {

    @ApiModelProperty(value = "考试ID")
    private String examId;
}

