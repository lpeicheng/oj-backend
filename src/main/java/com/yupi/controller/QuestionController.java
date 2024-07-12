package com.yupi.controller;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.yupi.constant.UserConstant;
import com.yupi.model.dto.question.QuestionEditRequest;
import com.yupi.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.yupi.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.yupi.model.entity.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.annotation.AuthCheck;
import com.yupi.common.BaseResponse;
import com.yupi.common.DeleteRequest;
import com.yupi.common.ErrorCode;
import com.yupi.common.ResultUtils;

import com.yupi.exception.BusinessException;
import com.yupi.exception.ThrowUtils;
import com.yupi.model.dto.question.QuestionAddRequest;
import com.yupi.model.dto.question.QuestionQueryRequest;
import com.yupi.model.dto.question.QuestionUpdateRequest;

import com.yupi.model.vo.QuestionSubmitVO;
import com.yupi.model.vo.QuestionVO;
import com.yupi.service.QuestionService;

import com.yupi.service.QuestionSubmitService;
import com.yupi.service.UserService;
import com.yupi.service.impl.QuestionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * 题目接口

 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;
    @Resource
    private UserService userService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    public final static Gson GSON=new Gson();
    /**
     * 创建题目
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        String title = questionAddRequest.getTitle();
        String content = questionAddRequest.getContent();
        List<String> tags = questionAddRequest.getTags();
        if (title==null||title.length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"标题不能为空且长度要小于20");
        }
        if (content==null||content.length()>100){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"内容不能为空且长度要小于100");
        }
        if (tags==null||tags.isEmpty()){
              throw new BusinessException(ErrorCode.PARAMS_ERROR,"标签不能为空");
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        if (judgeCase==null){
              throw new BusinessException(ErrorCode.PARAMS_ERROR,"测试用例不能为空");
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (judgeConfig==null){
              throw new BusinessException(ErrorCode.PARAMS_ERROR,"配置不能为空");
        }
        //存放时转成json（string），用的时候用JSON.toBEAN（）转成List<JudgeCase>类型
        question.setJudgeCase(GSON.toJson(judgeCase));
        question.setJudgeConfig(GSON.toJson(judgeConfig));
        question.setTags(GSON.toJson(tags));
        question.setUserId(userService.getLoginUser(request).getId());
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(question.getId());
    }

    /**
     * 删除题目
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question id = questionService.getById(deleteRequest.getId());
        if(id==null){
             throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        if(!id.getUserId().equals(userService.getLoginUser(request).getId())||!userService.getLoginUser(request).getUserRole().equals(UserConstant.ADMIN_ROLE)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"无权限删除");
        }
        boolean b = questionService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 修改题目
     *
     * @param questionUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest,
            HttpServletRequest request) {
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
         String title = questionUpdateRequest.getTitle();
         String content = questionUpdateRequest.getContent();
         List<String> tags = questionUpdateRequest.getTags();
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
         List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        if (questionUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(questionService.getById(questionUpdateRequest.getId())==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        if (title==null||title.length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"标题不能为空且长度要小于20");
        }
        if (content==null||content.length()>100){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"内容不能为空且长度要小于100");
        }
        if (tags==null||tags.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"标签不能为空");
        }
        if( judgeConfig!=null){
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        if (judgeCase != null) {
                question.setJudgeCase(GSON.toJson(judgeCase));
        }
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    //用户更新
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionUpdateRequest questionEditRequest,
                                                HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(questionService.getById(questionEditRequest.getId())==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        if (!questionService.getById(questionEditRequest.getId()).getUserId().equals(userService.getLoginUser(request).getId())||!userService.getLoginUser(request).getUserRole().equals(UserConstant.ADMIN_ROLE)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"无权限修改");
        }
        if (questionEditRequest.getContent()==null||questionEditRequest.getContent().length()>100){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"内容不能为空且长度要小于100");
        }
        if (questionEditRequest.getTags()==null||questionEditRequest.getTags().isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"标签不能为空");
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionEditRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionEditRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        question.setTags(GSON.toJson(questionEditRequest.getTags()));
        question.setJudgeCase(GSON.toJson(questionEditRequest.getJudgeCase()));
        question.setJudgeConfig(GSON.toJson(questionEditRequest.getJudgeConfig()));
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题目
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!userService.getLoginUser(request).getUserRole().equals(UserConstant.ADMIN_ROLE)){
             throw new BusinessException(ErrorCode.OPERATION_ERROR,"无权限查看");
        }
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(question);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 分页获取题目列表（仅管理员）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
            HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取题目封装列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
            HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage,request));
    }

    // endregion

    /**
     * 获取个人创建的题目
     *
     * @param queryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest queryRequest,
            HttpServletRequest request) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        queryRequest.setUserId(loginUser.getId());
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(queryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage,request));
    }

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @return 提交记录的 id
     */
    @PostMapping("/question_submit/do")
    public BaseResponse<Long> doSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,HttpServletRequest request){
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/question_submit/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        User loginUser = userService.getLoginUser(request);
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }
}
