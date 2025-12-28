package com.yf.exam.modules.user.exam.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 选择题选项VO（序列化JSON用）
 */
@Data
public class OptionVO implements Serializable {
    // 选项标识（A/B/C/对/错）
    private String label;
    // 选项内容
    private String content;
    // 是否为正确答案
    private Boolean isCorrect;
}
