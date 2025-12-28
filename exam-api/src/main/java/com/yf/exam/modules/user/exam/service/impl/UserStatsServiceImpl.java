package com.yf.exam.modules.user.exam.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.modules.user.exam.dto.ExamStatsDTO;
import com.yf.exam.modules.user.exam.dto.OptionVO;
import com.yf.exam.modules.user.exam.dto.UserAnswerDetailDTO;
import com.yf.exam.modules.user.exam.dto.request.UserExamReqDTO;
import com.yf.exam.modules.user.exam.dto.request.UserPaperReqDTO;
import com.yf.exam.modules.user.exam.dto.request.UserStatsReqDTO;
import com.yf.exam.modules.user.exam.dto.response.UserExamRespDTO;
import com.yf.exam.modules.user.exam.entity.UserExam;
import com.yf.exam.modules.user.exam.mapper.UserExamMapper;
import com.yf.exam.modules.user.exam.mapper.UserStatsMapper;
import com.yf.exam.modules.user.exam.service.UserStatsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStatsServiceImpl extends ServiceImpl<UserExamMapper, UserExam> implements UserStatsService{

    private final UserStatsMapper statsMapper;
    private final ObjectMapper objectMapper;

    @Override
    public IPage<UserExamRespDTO> itemlist(PagingReqDTO<UserExamReqDTO> reqDTO) {
        UserExamReqDTO params = reqDTO.getParams();
        if (params == null) {
            params = new UserExamReqDTO();
        }
        IPage<UserExamRespDTO> pageData = baseMapper.paging(reqDTO.toPage(), params);
        return pageData;
    }

    @Override
    public List<ExamStatsDTO> common(UserStatsReqDTO reqDTO){
        String userId = reqDTO.getUserId();
        String examId = reqDTO.getExamId();

        // 1. 调用 Mapper 获取题型统计
        List<ExamStatsDTO> statsList = statsMapper.statsByUserAndExam(userId, examId);

        return statsList;
    }

    @Override
    public List<UserAnswerDetailDTO> result(UserPaperReqDTO reqDTO){
        String userId = reqDTO.getUserId();
        String paperId = reqDTO.getPaperId();

        // 1. 调用 Mapper 获取题型统计
        List<Map<String, Object>> rawList = statsMapper.findAnswerDetails(userId, paperId);
        if (CollectionUtils.isEmpty(rawList)) {
            return new ArrayList<>();
        }

        List<UserAnswerDetailDTO> result = new ArrayList<>();
        for (Map<String, Object> raw : rawList) {
            UserAnswerDetailDTO dto = new UserAnswerDetailDTO();
            // 基础字段赋值
            dto.setQuId((String) raw.get("quId"));
            dto.setQuType((Integer) raw.get("quType"));
            dto.setQuContent((String) raw.get("quContent"));
            dto.setAnswer((String) raw.get("answer"));
            dto.setAnswered((Integer) raw.get("answered"));
            dto.setScore((Integer) raw.get("score"));
            dto.setIsRight((Integer) raw.get("isRight"));
            dto.setQuAnalysis((String) raw.get("quAnalysis"));

            // 3. 按题型处理correctAnswer
            Integer quType = (Integer) raw.get("quType");
            if (isChoiceQuestion(quType)) {
                // 选择类题目：序列化选项列表为JSON字符串
                dto.setCorrectAnswer(buildChoice(raw));
            } else {
                // 非选择类题目：直接返回字符串（填空/简答）
                dto.setCorrectAnswer((String) raw.get("correctAnswerStr"));
            }

            result.add(dto);
        }
        return result;
    }

    private boolean isChoiceQuestion(Integer quType) {
        // 1=单选，2=多选，3=判断
        return quType != null && (quType == 1 || quType == 2 || quType == 3);
    }

    private OptionVO buildOptionVO(String label, String content, boolean isCorrect) {
        OptionVO vo = new OptionVO();
        vo.setLabel(label);
        vo.setContent(content);
        vo.setIsCorrect(isCorrect);
        return vo;
    }

    private String buildChoice(Map<String, Object> raw) {
        List<OptionVO> optionList = new ArrayList<>();
        String optionsStr = (String) raw.get("options");
        String correctAnswerStr = (String) raw.get("correctAnswerStr");

        if (!StringUtils.hasText(optionsStr)) {
            try {
                return objectMapper.writeValueAsString(optionList); // 返回空数组 JSON：[]
            } catch (JsonProcessingException e) {
                return "[]"; // 兜底返回空数组
            }
        }

        String[] options = optionsStr.split(";");
        // 解析正确答案标识（如 B,C → 数组）
        List<String> correctLabels = new ArrayList<>();
        if (StringUtils.hasText(correctAnswerStr)) {
            String[] labels = correctAnswerStr.split(",");
            for (String label : labels) {
                correctLabels.add(label.trim());
            }
        }
        // 遍历选项，封装OptionVO
        for (String opt : options) {
            String[] optArr = opt.split(":");
            if (optArr.length < 3) {
                continue;
            }
            String label = optArr[0].trim();
            String content = optArr[1].trim();
            // 判断是否为正确答案
            boolean isCorrect = correctLabels.contains(label);
            optionList.add(buildOptionVO(label, content, isCorrect));
        }
        // 3. 序列化为JSON字符串
        try {
            return objectMapper.writeValueAsString(optionList);
        } catch (JsonProcessingException e) {
            // 序列化失败时返回空字符串
            return "";
        }
    }
}
