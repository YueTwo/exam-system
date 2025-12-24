package com.yf.exam.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.core.enums.OpenType;
import com.yf.exam.core.exception.ServiceException;
import com.yf.exam.core.utils.BeanMapper;
import com.yf.exam.modules.exam.dto.ExamDTO;
import com.yf.exam.modules.exam.dto.ExamRepoDTO;
import com.yf.exam.modules.exam.dto.ext.ExamRepoExtDTO;
import com.yf.exam.modules.exam.dto.request.ExamSaveReqDTO;
import com.yf.exam.modules.exam.dto.response.ExamOnlineRespDTO;
import com.yf.exam.modules.exam.dto.response.ExamReviewRespDTO;
import com.yf.exam.modules.exam.dto.response.ExamScoreStatDTO;
import com.yf.exam.modules.exam.entity.Exam;
import com.yf.exam.modules.exam.entity.ExamSetting;
import com.yf.exam.modules.exam.mapper.ExamMapper;
import com.yf.exam.modules.exam.service.ExamDepartService;
import com.yf.exam.modules.exam.service.ExamRepoService;
import com.yf.exam.modules.exam.service.ExamSettingService;
import com.yf.exam.modules.exam.service.ExamService;
import com.yf.exam.modules.user.exam.dto.request.UserExamReqDTO;
import com.yf.exam.modules.user.exam.dto.response.UserExamRespDTO;
import com.yf.exam.modules.user.exam.service.UserExamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
* <p>
* 考试业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-07-25 16:18
*/
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {


    @Autowired
    private ExamRepoService examRepoService;

    @Autowired
    private ExamDepartService examDepartService;

    @Autowired
    private UserExamService userExamService;

    @Autowired
    private ExamSettingService examSettingService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(ExamSaveReqDTO reqDTO) {

        if(reqDTO.getTimeLimit()!=null && reqDTO.getTimeLimit()){
            if(reqDTO.getStartTime() == null || reqDTO.getEndTime() == null){
                throw new ServiceException(1, "限时考试需要设置开始和结束时间！");
            }
            if(reqDTO.getEndTime().before(reqDTO.getStartTime())){
                throw new ServiceException(1, "结束时间必须晚于开始时间！");
            }
        }

        // ID
        String id = reqDTO.getId();

        if(StringUtils.isBlank(id)){
            id = IdWorker.getIdStr();
        }

        //复制参数
        Exam entity = new Exam();

        // 计算分值
        this.calcScore(reqDTO);


        // 复制基本数据
        BeanMapper.copy(reqDTO, entity);
        entity.setId(id);

        // 修复状态
        if (reqDTO.getTimeLimit()!=null
                && !reqDTO.getTimeLimit()
                && reqDTO.getState()!=null
                && reqDTO.getState() == 2) {
            entity.setState(0);
        } else {
            entity.setState(reqDTO.getState());
        }

        // 题库组卷
        try {
            examRepoService.saveAll(id, reqDTO.getRepoList());
        }catch (DuplicateKeyException e){
            throw new ServiceException(1, "不能选择重复的题库！");
        }


        // 开放的部门
        if(OpenType.DEPT_OPEN.equals(reqDTO.getOpenType())){
            examDepartService.saveAll(id, reqDTO.getDepartIds());
        } else {
            examDepartService.clear(id);
        }

        this.saveOrUpdate(entity);
        examSettingService.saveOrUpdateByExamId(id, reqDTO);

    }

    @Override
    public ExamSaveReqDTO findDetail(String id) {
        ExamSaveReqDTO respDTO = new ExamSaveReqDTO();
        Exam exam = this.getById(id);
        BeanMapper.copy(exam, respDTO);

        // 扩展配置
        ExamSetting setting = examSettingService.getOrDefault(id);
        respDTO.setAllowLate(setting.getAllowLate());
        respDTO.setExamNotice(setting.getExamNotice());
        respDTO.setResultShowType(setting.getResultShowType());
        respDTO.setThankText(setting.getThankText());
        respDTO.setMaxTryCount(setting.getMaxTryCount());
        respDTO.setRewardPoints(setting.getRewardPoints());
        respDTO.setMinSubmitMinutes(setting.getMinSubmitMinutes());
        respDTO.setPriceCent(setting.getPriceCent());

        // 考试部门
        List<String> departIds = examDepartService.listByExam(id);
        respDTO.setDepartIds(departIds);

        // 题库
        List<ExamRepoExtDTO> repos = examRepoService.listByExam(id);
        respDTO.setRepoList(repos);

        return respDTO;
    }

    @Override
    public ExamDTO findById(String id) {
        ExamDTO respDTO = new ExamDTO();
        Exam exam = this.getById(id);
        BeanMapper.copy(exam, respDTO);
        return respDTO;
    }

    @Override
    public IPage<ExamDTO> paging(PagingReqDTO<ExamDTO> reqDTO) {

        //创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());

        //转换结果
        IPage<ExamDTO> pageData = baseMapper.paging(page, reqDTO.getParams());
        return pageData;
     }

    @Override
    public IPage<ExamOnlineRespDTO> onlinePaging(PagingReqDTO<ExamDTO> reqDTO) {


        // 创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());

        // 查找分页
        IPage<ExamOnlineRespDTO> pageData = baseMapper.online(page, reqDTO.getParams());

        return pageData;
    }

    @Override
    public IPage<ExamReviewRespDTO> reviewPaging(PagingReqDTO<ExamDTO> reqDTO) {
        // 创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());

        // 查找分页
        IPage<ExamReviewRespDTO> pageData = baseMapper.reviewPaging(page, reqDTO.getParams());

        return pageData;
    }


    @Override
    public IPage<UserExamRespDTO> scorePaging(PagingReqDTO<UserExamReqDTO> reqDTO) {
        UserExamReqDTO params = reqDTO.getParams();
        if(params == null || StringUtils.isBlank(params.getExamId())){
            throw new ServiceException(1, "请选择考试！");
        }
        return userExamService.paging(reqDTO);
    }

    @Override
    public ExamScoreStatDTO scoreStat(String examId) {
        if(StringUtils.isBlank(examId)){
            throw new ServiceException(1, "考试ID不能为空！");
        }
        ExamScoreStatDTO stat = baseMapper.scoreStat(examId);
        if(stat == null){
            stat = new ExamScoreStatDTO();
            stat.setExamId(examId);
            stat.setTotalUser(0);
            stat.setPassUser(0);
            stat.setAvgScore(BigDecimal.ZERO);
            stat.setMaxScore(0);
            stat.setMinScore(0);
            stat.setPassRate(BigDecimal.ZERO);
        }else{
            if(stat.getTotalUser()==null){
                stat.setTotalUser(0);
            }
            if(stat.getPassUser()==null){
                stat.setPassUser(0);
            }
            if(stat.getAvgScore()==null){
                stat.setAvgScore(BigDecimal.ZERO);
            }
            if(stat.getMaxScore()==null){
                stat.setMaxScore(0);
            }
            if(stat.getMinScore()==null){
                stat.setMinScore(0);
            }
            if(stat.getPassRate()==null){
                stat.setPassRate(BigDecimal.ZERO);
            }
        }
        return stat;
    }


    /**
     * 计算分值
     * @param reqDTO
     */
    private void calcScore(ExamSaveReqDTO reqDTO){

        // 主观题分数
        int objScore = 0;

        // 题库组卷
        List<ExamRepoExtDTO> repoList = reqDTO.getRepoList();

        if(CollectionUtils.isEmpty(repoList)){
            throw new ServiceException(1, "必须选择至少一个题库！");
        }

        for(ExamRepoDTO item: repoList){
            if(item.getRadioCount()!=null
                    && item.getRadioCount()>0
                    && item.getRadioScore()!=null
                    && item.getRadioScore()>0){
                objScore+=item.getRadioCount()*item.getRadioScore();
            }
            if(item.getMultiCount()!=null
                    && item.getMultiCount()>0
                    && item.getMultiScore()!=null
                    && item.getMultiScore()>0){
                objScore+=item.getMultiCount()*item.getMultiScore();
            }
            if(item.getJudgeCount()!=null
                    && item.getJudgeCount()>0
                    && item.getJudgeScore()!=null
                    && item.getJudgeScore()>0){
                objScore+=item.getJudgeCount()*item.getJudgeScore();
            }
        }



        reqDTO.setTotalScore(objScore);
    }

}
