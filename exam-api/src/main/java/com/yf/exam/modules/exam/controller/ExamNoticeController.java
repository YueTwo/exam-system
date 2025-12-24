package com.yf.exam.modules.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.exam.core.api.ApiRest;
import com.yf.exam.core.api.controller.BaseController;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.modules.exam.dto.request.ExamNoticeQueryDTO;
import com.yf.exam.modules.exam.dto.request.ExamNoticeSendReqDTO;
import com.yf.exam.modules.exam.entity.ExamNotice;
import com.yf.exam.modules.exam.service.ExamNoticeService;
import com.yf.exam.modules.user.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"考试通知"})
@RestController
@RequestMapping("/exam/api/exam/notice")
public class ExamNoticeController extends BaseController {

    @Autowired
    private ExamNoticeService examNoticeService;

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "发送通知")
    @RequestMapping(value = "/send", method = {RequestMethod.POST})
    public ApiRest send(@RequestBody ExamNoticeSendReqDTO reqDTO) {
        examNoticeService.send(UserUtils.getUserId(), reqDTO);
        return success();
    }

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "通知分页")
    @RequestMapping(value = "/paging", method = {RequestMethod.POST})
    public ApiRest<IPage<ExamNotice>> paging(@RequestBody PagingReqDTO<ExamNoticeQueryDTO> reqDTO) {
        return success(examNoticeService.paging(reqDTO));
    }
}

