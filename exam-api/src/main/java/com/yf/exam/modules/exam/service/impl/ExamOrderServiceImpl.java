package com.yf.exam.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.core.exception.ServiceException;
import com.yf.exam.modules.exam.dto.request.ExamOrderCreateReqDTO;
import com.yf.exam.modules.exam.dto.request.ExamOrderQueryDTO;
import com.yf.exam.modules.exam.dto.response.ExamOrderRespDTO;
import com.yf.exam.modules.exam.entity.ExamOrder;
import com.yf.exam.modules.exam.entity.ExamSetting;
import com.yf.exam.modules.exam.mapper.ExamOrderMapper;
import com.yf.exam.modules.exam.service.ExamOrderService;
import com.yf.exam.modules.exam.service.ExamSettingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExamOrderServiceImpl extends ServiceImpl<ExamOrderMapper, ExamOrder> implements ExamOrderService {

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_PAID = 1;
    public static final int STATUS_CANCELED = 2;

    @Autowired
    private ExamSettingService examSettingService;

    @Override
    public ExamOrder createOrder(String userId, ExamOrderCreateReqDTO reqDTO) {
        if (StringUtils.isBlank(userId)) {
            throw new ServiceException(1, "用户未登录！");
        }
        if (reqDTO == null || StringUtils.isBlank(reqDTO.getExamId())) {
            throw new ServiceException(1, "考试ID不能为空！");
        }

        ExamSetting setting = examSettingService.getOrDefault(reqDTO.getExamId());
        int amount = setting.getPriceCent() == null ? 0 : setting.getPriceCent();
        if (amount <= 0) {
            throw new ServiceException(1, "该考试为免费考试，无需下单！");
        }

        QueryWrapper<ExamOrder> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ExamOrder::getUserId, userId).eq(ExamOrder::getExamId, reqDTO.getExamId());
        ExamOrder order = this.getOne(wrapper, false);
        Date now = new Date();
        if (order == null) {
            order = new ExamOrder();
            order.setUserId(userId);
            order.setExamId(reqDTO.getExamId());
            order.setAmountCent(amount);
            order.setStatus(STATUS_PENDING);
            order.setCreateTime(now);
            order.setUpdateTime(now);
            this.save(order);
            return order;
        }

        if (order.getStatus() != null && order.getStatus() == STATUS_PAID) {
            return order;
        }

        order.setAmountCent(amount);
        order.setStatus(STATUS_PENDING);
        order.setUpdateTime(now);
        this.updateById(order);
        return order;
    }

    @Override
    public void markPaid(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new ServiceException(1, "订单ID不能为空！");
        }
        ExamOrder order = this.getById(orderId);
        if (order == null) {
            throw new ServiceException(1, "订单不存在！");
        }
        if (order.getStatus() != null && order.getStatus() == STATUS_PAID) {
            return;
        }
        Date now = new Date();
        order.setStatus(STATUS_PAID);
        order.setPayTime(now);
        order.setUpdateTime(now);
        this.updateById(order);
    }

    @Override
    public boolean hasPaid(String userId, String examId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(examId)) {
            return false;
        }
        QueryWrapper<ExamOrder> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(ExamOrder::getUserId, userId)
                .eq(ExamOrder::getExamId, examId)
                .eq(ExamOrder::getStatus, STATUS_PAID);
        return this.count(wrapper) > 0;
    }

    @Override
    public IPage<ExamOrderRespDTO> paging(PagingReqDTO<ExamOrderQueryDTO> reqDTO) {
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());
        return baseMapper.paging(page, reqDTO.getParams());
    }
}
