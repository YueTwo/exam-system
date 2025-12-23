package com.yf.exam.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * 考试扩展配置
 */
@Data
@TableName("el_exam_setting")
public class ExamSetting extends Model<ExamSetting> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "exam_id", type = IdType.INPUT)
    private String examId;

    @TableField("allow_late")
    private Boolean allowLate;

    @TableField("exam_notice")
    private String examNotice;

    @TableField("result_show_type")
    private Integer resultShowType;

    @TableField("thank_text")
    private String thankText;

    @TableField("max_try_count")
    private Integer maxTryCount;

    @TableField("reward_points")
    private Integer rewardPoints;

    @TableField("min_submit_minutes")
    private Integer minSubmitMinutes;

    @TableField("price_cent")
    private Integer priceCent;

    @TableField("share_enabled")
    private Boolean shareEnabled;

    @TableField("share_token")
    private String shareToken;

    @TableField("share_expire_time")
    private Date shareExpireTime;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}

