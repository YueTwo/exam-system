package com.yf.exam.modules.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.exam.core.api.ApiRest;
import com.yf.exam.core.api.controller.BaseController;
import com.yf.exam.core.api.dto.BaseIdRespDTO;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.core.exception.ServiceException;
import com.yf.exam.modules.exam.dto.request.ExamOrderCreateReqDTO;
import com.yf.exam.modules.exam.dto.request.ExamOrderMarkPaidReqDTO;
import com.yf.exam.modules.exam.dto.request.ExamOrderQueryDTO;
import com.yf.exam.modules.exam.dto.request.ExamPriceSaveReqDTO;
import com.yf.exam.modules.exam.dto.response.ExamOrderRespDTO;
import com.yf.exam.modules.exam.entity.ExamSetting;
import com.yf.exam.modules.exam.service.ExamOrderService;
import com.yf.exam.modules.exam.service.ExamSettingService;
import com.yf.exam.modules.user.UserUtils;
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

@Api(tags = {"考试付费"})
@RestController
@RequestMapping("/exam/api/exam/pay")
public class ExamPayController extends BaseController {

    @Autowired
    private ExamSettingService examSettingService;

    @Autowired
    private ExamOrderService examOrderService;

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "配置售价")
    @RequestMapping(value = "/price/save", method = {RequestMethod.POST})
    public ApiRest savePrice(@RequestBody ExamPriceSaveReqDTO reqDTO) {
        if (reqDTO == null || StringUtils.isBlank(reqDTO.getExamId())) {
            throw new ServiceException(1, "考试ID不能为空！");
        }
        ExamSetting setting = examSettingService.getOrDefault(reqDTO.getExamId());
        setting.setPriceCent(reqDTO.getPriceCent() == null ? 0 : Math.max(reqDTO.getPriceCent(), 0));
        setting.setUpdateTime(new Date());
        examSettingService.saveOrUpdate(setting);
        return success();
    }

    @ApiOperation(value = "创建订单（学生）")
    @RequestMapping(value = "/order/create", method = {RequestMethod.POST})
    public ApiRest<BaseIdRespDTO> createOrder(@RequestBody ExamOrderCreateReqDTO reqDTO) {
        String userId = UserUtils.getUserId();
        return success(new BaseIdRespDTO(examOrderService.createOrder(userId, reqDTO).getId()));
    }

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "订单分页（管理端）")
    @RequestMapping(value = "/order/paging", method = {RequestMethod.POST})
    public ApiRest<IPage<ExamOrderRespDTO>> paging(@RequestBody PagingReqDTO<ExamOrderQueryDTO> reqDTO) {
        return success(examOrderService.paging(reqDTO));
    }

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "标记订单已支付")
    @RequestMapping(value = "/order/mark-paid", method = {RequestMethod.POST})
    public ApiRest markPaid(@RequestBody ExamOrderMarkPaidReqDTO reqDTO) {
        if (reqDTO == null || StringUtils.isBlank(reqDTO.getOrderId())) {
            throw new ServiceException(1, "订单ID不能为空！");
        }
        examOrderService.markPaid(reqDTO.getOrderId());
        return success();
    }
}

