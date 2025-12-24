package com.yf.exam.modules.exam.controller;

import com.yf.exam.core.api.ApiRest;
import com.yf.exam.core.api.controller.BaseController;
import com.yf.exam.modules.exam.dto.request.ExamAnalysisReqDTO;
import com.yf.exam.modules.exam.dto.response.ExamDepartOverviewDTO;
import com.yf.exam.modules.exam.dto.response.ExamPersonOverviewDTO;
import com.yf.exam.modules.exam.dto.response.ExamWrongOverviewDTO;
import com.yf.exam.modules.exam.mapper.ExamAnalysisMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"统计分析"})
@RestController
@RequestMapping("/exam/api/exam/analysis")
public class ExamAnalysisController extends BaseController {

    @Autowired
    private ExamAnalysisMapper examAnalysisMapper;

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "人员概览")
    @RequestMapping(value = "/person-overview", method = {RequestMethod.POST})
    public ApiRest<List<ExamPersonOverviewDTO>> personOverview(@RequestBody(required = false) ExamAnalysisReqDTO reqDTO) {
        String examId = reqDTO == null ? null : reqDTO.getExamId();
        int top = (reqDTO == null || reqDTO.getTop() == null || reqDTO.getTop() <= 0) ? 20 : reqDTO.getTop();
        return success(examAnalysisMapper.personOverview(examId, top));
    }

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "部门概览")
    @RequestMapping(value = "/depart-overview", method = {RequestMethod.POST})
    public ApiRest<List<ExamDepartOverviewDTO>> departOverview(@RequestBody(required = false) ExamAnalysisReqDTO reqDTO) {
        String examId = reqDTO == null ? null : reqDTO.getExamId();
        int top = (reqDTO == null || reqDTO.getTop() == null || reqDTO.getTop() <= 0) ? 20 : reqDTO.getTop();
        return success(examAnalysisMapper.departOverview(examId, top));
    }

    @RequiresRoles(value = {"teacher", "assistant"}, logical = Logical.OR)
    @ApiOperation(value = "错题分析")
    @RequestMapping(value = "/wrong-overview", method = {RequestMethod.POST})
    public ApiRest<List<ExamWrongOverviewDTO>> wrongOverview(@RequestBody(required = false) ExamAnalysisReqDTO reqDTO) {
        String examId = reqDTO == null ? null : reqDTO.getExamId();
        int top = (reqDTO == null || reqDTO.getTop() == null || reqDTO.getTop() <= 0) ? 20 : reqDTO.getTop();
        return success(examAnalysisMapper.wrongOverview(examId, top));
    }
}

