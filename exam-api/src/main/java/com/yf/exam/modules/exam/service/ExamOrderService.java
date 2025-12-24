package com.yf.exam.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.modules.exam.dto.request.ExamOrderCreateReqDTO;
import com.yf.exam.modules.exam.dto.request.ExamOrderQueryDTO;
import com.yf.exam.modules.exam.dto.response.ExamOrderRespDTO;
import com.yf.exam.modules.exam.entity.ExamOrder;

public interface ExamOrderService extends IService<ExamOrder> {

    ExamOrder createOrder(String userId, ExamOrderCreateReqDTO reqDTO);

    void markPaid(String orderId);

    boolean hasPaid(String userId, String examId);

    IPage<ExamOrderRespDTO> paging(PagingReqDTO<ExamOrderQueryDTO> reqDTO);
}

