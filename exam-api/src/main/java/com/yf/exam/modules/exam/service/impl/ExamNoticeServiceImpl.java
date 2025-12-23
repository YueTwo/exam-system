package com.yf.exam.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.core.exception.ServiceException;
import com.yf.exam.modules.exam.dto.request.ExamNoticeQueryDTO;
import com.yf.exam.modules.exam.dto.request.ExamNoticeSendReqDTO;
import com.yf.exam.modules.exam.entity.ExamNotice;
import com.yf.exam.modules.exam.mapper.ExamNoticeMapper;
import com.yf.exam.modules.exam.service.ExamNoticeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExamNoticeServiceImpl extends ServiceImpl<ExamNoticeMapper, ExamNotice> implements ExamNoticeService {

    @Override
    public void send(String userId, ExamNoticeSendReqDTO reqDTO) {
        if (StringUtils.isBlank(userId)) {
            throw new ServiceException(1, "用户未登录！");
        }
        if (reqDTO == null || StringUtils.isBlank(reqDTO.getExamId())) {
            throw new ServiceException(1, "考试ID不能为空！");
        }
        if (StringUtils.isBlank(reqDTO.getTitle())) {
            throw new ServiceException(1, "通知标题不能为空！");
        }
        if (StringUtils.isBlank(reqDTO.getContent())) {
            throw new ServiceException(1, "通知内容不能为空！");
        }
        ExamNotice notice = new ExamNotice();
        notice.setExamId(reqDTO.getExamId());
        notice.setTitle(reqDTO.getTitle());
        notice.setContent(reqDTO.getContent());
        notice.setCreateUserId(userId);
        notice.setCreateTime(new Date());
        this.save(notice);
    }

    @Override
    public IPage<ExamNotice> paging(PagingReqDTO<ExamNoticeQueryDTO> reqDTO) {
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());
        QueryWrapper<ExamNotice> wrapper = new QueryWrapper<>();
        if (reqDTO.getParams() != null && StringUtils.isNotBlank(reqDTO.getParams().getExamId())) {
            wrapper.lambda().eq(ExamNotice::getExamId, reqDTO.getParams().getExamId());
        }
        wrapper.lambda().orderByDesc(ExamNotice::getCreateTime);
        return this.page(page, wrapper);
    }
}

