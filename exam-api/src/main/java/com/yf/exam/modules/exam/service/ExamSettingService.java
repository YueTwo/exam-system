package com.yf.exam.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.exam.modules.exam.dto.request.ExamSaveReqDTO;
import com.yf.exam.modules.exam.entity.ExamSetting;

public interface ExamSettingService extends IService<ExamSetting> {

    ExamSetting getOrDefault(String examId);

    void saveOrUpdateByExamId(String examId, ExamSaveReqDTO reqDTO);

    ExamSetting enableShare(String examId, java.util.Date expireTime);

    ExamSetting disableShare(String examId);

    ExamSetting findByShareToken(String shareToken);
}
