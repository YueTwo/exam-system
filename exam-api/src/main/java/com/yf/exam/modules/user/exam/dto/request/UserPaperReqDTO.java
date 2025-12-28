package com.yf.exam.modules.user.exam.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPaperReqDTO {
    @ApiModelProperty("试卷ID")
    private String paperId;

    @ApiModelProperty("用户ID")
    private String userId;
}
