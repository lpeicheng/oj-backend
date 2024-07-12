package com.yupi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.yupi.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.yupi.model.entity.Question;
import com.yupi.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.model.entity.User;
import com.yupi.model.vo.QuestionSubmitVO;
import com.yupi.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author kuang
* @description 针对表【question_submit(用户提交表)】的数据库操作Service
* @createDate 2024-07-06 17:08:18
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionPage, User loginUser);

    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

}
