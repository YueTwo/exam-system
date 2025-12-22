package com.yf.exam.modules.user.exam.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserStatsReqDTO {

    @ApiModelProperty("考试ID")
    private String examId;

    @ApiModelProperty("用户ID")
    private String userId;
}