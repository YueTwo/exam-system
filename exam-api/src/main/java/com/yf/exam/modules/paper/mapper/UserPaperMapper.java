package com.yf.exam.modules.paper.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.exam.modules.paper.entity.UserExamAttempt;

@Mapper
public interface UserPaperMapper extends BaseMapper<UserExamAttempt>{

}
