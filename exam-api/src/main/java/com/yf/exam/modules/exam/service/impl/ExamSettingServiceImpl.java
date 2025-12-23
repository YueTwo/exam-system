package com.yf.exam.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.exam.modules.exam.dto.request.ExamSaveReqDTO;
import com.yf.exam.modules.exam.entity.ExamSetting;
import com.yf.exam.modules.exam.mapper.ExamSettingMapper;
import com.yf.exam.modules.exam.service.ExamSettingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ExamSettingServiceImpl extends ServiceImpl<ExamSettingMapper, ExamSetting> implements ExamSettingService {

    private static final int RESULT_SHOW_THANK_ONLY = 1;
    private static final int RESULT_SHOW_THANK_AND_SCORE = 2;

    @Override
    public ExamSetting getOrDefault(String examId) {
        if (StringUtils.isBlank(examId)) {
            return defaultSetting(null);
        }
        ExamSetting setting = this.getById(examId);
        if (setting == null) {
            return defaultSetting(examId);
        }
        normalize(setting);
        return setting;
    }

    @Override
    public void saveOrUpdateByExamId(String examId, ExamSaveReqDTO reqDTO) {
        if (StringUtils.isBlank(examId)) {
            return;
        }
        ExamSetting setting = this.getById(examId);
        Date now = new Date();
        if (setting == null) {
            setting = defaultSetting(examId);
            setting.setCreateTime(now);
        }

        if (reqDTO != null) {
            setting.setAllowLate(reqDTO.getAllowLate());
            setting.setExamNotice(reqDTO.getExamNotice());
            setting.setResultShowType(reqDTO.getResultShowType());
            setting.setThankText(reqDTO.getThankText());
            setting.setMaxTryCount(reqDTO.getMaxTryCount());
            setting.setRewardPoints(reqDTO.getRewardPoints());
            setting.setMinSubmitMinutes(reqDTO.getMinSubmitMinutes());
            setting.setPriceCent(reqDTO.getPriceCent());
        }

        setting.setUpdateTime(now);
        normalize(setting);
        this.saveOrUpdate(setting);
    }

    @Override
    public ExamSetting enableShare(String examId, Date expireTime) {
        if (StringUtils.isBlank(examId)) {
            return null;
        }
        ExamSetting setting = this.getById(examId);
        Date now = new Date();
        if (setting == null) {
            setting = defaultSetting(examId);
            setting.setCreateTime(now);
        }
        if (StringUtils.isBlank(setting.getShareToken())) {
            setting.setShareToken(UUID.randomUUID().toString().replace("-", ""));
        }
        setting.setShareEnabled(true);
        setting.setShareExpireTime(expireTime);
        setting.setUpdateTime(now);
        normalize(setting);
        this.saveOrUpdate(setting);
        return setting;
    }

    @Override
    public ExamSetting disableShare(String examId) {
        if (StringUtils.isBlank(examId)) {
            return null;
        }
        ExamSetting setting = this.getById(examId);
        if (setting == null) {
            return defaultSetting(examId);
        }
        setting.setShareEnabled(false);
        setting.setUpdateTime(new Date());
        this.saveOrUpdate(setting);
        normalize(setting);
        return setting;
    }

    @Override
    public ExamSetting findByShareToken(String shareToken) {
        if (StringUtils.isBlank(shareToken)) {
            return null;
        }
        QueryWrapper<ExamSetting> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ExamSetting::getShareToken, shareToken);
        ExamSetting setting = this.getOne(wrapper, false);
        if (setting == null) {
            return null;
        }
        normalize(setting);
        return setting;
    }

    private static ExamSetting defaultSetting(String examId) {
        ExamSetting setting = new ExamSetting();
        setting.setExamId(examId);
        setting.setAllowLate(true);
        setting.setExamNotice("");
        setting.setResultShowType(RESULT_SHOW_THANK_AND_SCORE);
        setting.setThankText("感谢参加考试！");
        setting.setMaxTryCount(0);
        setting.setRewardPoints(0);
        setting.setMinSubmitMinutes(0);
        setting.setPriceCent(0);
        setting.setShareEnabled(false);
        return setting;
    }

    private static void normalize(ExamSetting setting) {
        if (setting.getAllowLate() == null) {
            setting.setAllowLate(true);
        }
        if (setting.getExamNotice() == null) {
            setting.setExamNotice("");
        }
        if (setting.getResultShowType() == null) {
            setting.setResultShowType(RESULT_SHOW_THANK_AND_SCORE);
        }
        if (setting.getResultShowType() != RESULT_SHOW_THANK_ONLY
                && setting.getResultShowType() != RESULT_SHOW_THANK_AND_SCORE) {
            setting.setResultShowType(RESULT_SHOW_THANK_AND_SCORE);
        }
        if (setting.getThankText() == null) {
            setting.setThankText("感谢参加考试！");
        }
        if (setting.getMaxTryCount() == null || setting.getMaxTryCount() < 0) {
            setting.setMaxTryCount(0);
        }
        if (setting.getRewardPoints() == null || setting.getRewardPoints() < 0) {
            setting.setRewardPoints(0);
        }
        if (setting.getMinSubmitMinutes() == null || setting.getMinSubmitMinutes() < 0) {
            setting.setMinSubmitMinutes(0);
        }
        if (setting.getPriceCent() == null || setting.getPriceCent() < 0) {
            setting.setPriceCent(0);
        }
        if (setting.getShareEnabled() == null) {
            setting.setShareEnabled(false);
        }
    }
}
