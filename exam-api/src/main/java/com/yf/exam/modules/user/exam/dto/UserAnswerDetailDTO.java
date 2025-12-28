package com.yf.exam.modules.user.exam.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 答题详情DTO（关联el_paper_qu + el_qu）
 */
@Data
@ApiModel(value = "答题详情DTO", description = "单道题的答题记录+题目信息（含未答题）")
public class UserAnswerDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ========== el_paper_qu 答题记录字段 ==========
    @ApiModelProperty(value = "答题记录ID")
    private String id;

    @ApiModelProperty(value = "试卷ID")
    private String paperId;

    @ApiModelProperty(value = "题目ID")
    private String quId;

    @ApiModelProperty(value = "题型（1=单选，2=多选，3=判断，4=填空，5=简答）")
    private Integer quType;

    @ApiModelProperty(value = "是否答题（0=未答，1=已答）")
    private Integer answered;

    @ApiModelProperty(value = "用户答案")
    private String answer;

    @ApiModelProperty(value = "题目排序")
    private Integer sort;

    @ApiModelProperty(value = "题目分值")
    private Integer score;

    @ApiModelProperty(value = "实际得分")
    private Integer actualScore;

    @ApiModelProperty(value = "是否正确（0=错误，1=正确）")
    private Integer isRight;

    // ========== el_qu 题目信息字段 ==========
    @ApiModelProperty(value = "题目难度等级")
    private Integer level;

    @ApiModelProperty(value = "题目图片（URL）")
    private String image;

    @ApiModelProperty(value = "题目内容")
    private String quContent; // 对应el_qu.content

    @ApiModelProperty(value = "题目解析")
    private String quAnalysis; // 对应el_qu.analysis

    @ApiModelProperty(value = "题目备注")
    private String quRemark;

    @ApiModelProperty(value = "正确答案（多选项用逗号分隔）")
    private String correctAnswer;
}
