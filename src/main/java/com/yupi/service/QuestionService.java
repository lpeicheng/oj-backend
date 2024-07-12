package com.yupi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.model.dto.question.QuestionQueryRequest;
import com.yupi.model.dto.user.UserQueryRequest;
import com.yupi.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.model.entity.User;
import com.yupi.model.vo.QuestionVO;
import com.yupi.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author kuang
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-07-06 17:08:18
*/
public interface QuestionService extends IService<Question> {
    QuestionVO getQuestionVO(Question question);
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest queryRequest);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);}
