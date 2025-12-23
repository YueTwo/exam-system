package com.yf.exam.modules.exam.controller;

import com.yf.exam.core.api.ApiRest;
import com.yf.exam.core.api.controller.BaseController;
import com.yf.exam.core.api.dto.BaseIdReqDTO;
import com.yf.exam.core.exception.ServiceException;
import com.yf.exam.modules.exam.dto.request.ExamShareGenerateReqDTO;
import com.yf.exam.modules.exam.dto.request.ExamShareResolveReqDTO;
import com.yf.exam.modules.exam.dto.response.ExamShareRespDTO;
import com.yf.exam.modules.exam.entity.ExamSetting;
import com.yf.exam.modules.exam.service.ExamSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Api(tags = {"考试分享"})
@RestController
@RequestMapping("/exam/api/exam/share")
public class ExamShareController extends BaseController {

    @Autowired
    private ExamSettingService examSettingService;

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "生成分享链接")
    @RequestMapping(value = "/generate", method = {RequestMethod.POST})
    public ApiRest<ExamShareRespDTO> generate(@RequestBody ExamShareGenerateReqDTO reqDTO) {
        if (reqDTO == null || StringUtils.isBlank(reqDTO.getExamId())) {
            throw new ServiceException(1, "考试ID不能为空！");
        }
        ExamSetting setting = examSettingService.enableShare(reqDTO.getExamId(), reqDTO.getExpireTime());
        return success(toResp(setting));
    }

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "禁用分享链接")
    @RequestMapping(value = "/disable", method = {RequestMethod.POST})
    public ApiRest<ExamShareRespDTO> disable(@RequestBody BaseIdReqDTO reqDTO) {
        if (reqDTO == null || StringUtils.isBlank(reqDTO.getId())) {
            throw new ServiceException(1, "考试ID不能为空！");
        }
        ExamSetting setting = examSettingService.disableShare(reqDTO.getId());
        return success(toResp(setting));
    }

    @ApiOperation(value = "解析分享Token")
    @RequestMapping(value = "/resolve", method = {RequestMethod.POST})
    public ApiRest<ExamShareRespDTO> resolve(@RequestBody ExamShareResolveReqDTO reqDTO) {
        if (reqDTO == null || StringUtils.isBlank(reqDTO.getShareToken())) {
            throw new ServiceException(1, "分享Token不能为空！");
        }
        ExamSetting setting = examSettingService.findByShareToken(reqDTO.getShareToken());
        if (setting == null) {
            throw new ServiceException(1, "分享链接不存在或已失效！");
        }
        if (Boolean.TRUE.equals(setting.getShareEnabled())
                && setting.getShareExpireTime() != null
                && setting.getShareExpireTime().before(new Date())) {
            throw new ServiceException(1, "分享链接已过期！");
        }
        return success(toResp(setting));
    }

    private static ExamShareRespDTO toResp(ExamSetting setting) {
        ExamShareRespDTO resp = new ExamShareRespDTO();
        if (setting == null) {
            return resp;
        }
        resp.setExamId(setting.getExamId());
        resp.setShareEnabled(setting.getShareEnabled());
        resp.setShareToken(setting.getShareToken());
        resp.setShareExpireTime(setting.getShareExpireTime());
        return resp;
    }
}

