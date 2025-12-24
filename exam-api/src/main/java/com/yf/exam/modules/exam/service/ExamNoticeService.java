package com.yf.exam.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.modules.exam.dto.request.ExamNoticeQueryDTO;
import com.yf.exam.modules.exam.dto.request.ExamNoticeSendReqDTO;
import com.yf.exam.modules.exam.entity.ExamNotice;

public interface ExamNoticeService extends IService<ExamNotice> {

    void send(String userId, ExamNoticeSendReqDTO reqDTO);

    IPage<ExamNotice> paging(PagingReqDTO<ExamNoticeQueryDTO> reqDTO);
}

