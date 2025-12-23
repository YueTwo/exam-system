package com.yf.exam.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
@TableName("el_exam_order")
public class ExamOrder extends Model<ExamOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("exam_id")
    private String examId;

    @TableField("user_id")
    private String userId;

    @TableField("amount_cent")
    private Integer amountCent;

    /**
     * 0-待支付 1-已支付 2-已取消
     */
    private Integer status;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("pay_time")
    private Date payTime;
}

