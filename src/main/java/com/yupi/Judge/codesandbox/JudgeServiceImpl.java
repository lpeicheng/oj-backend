package com.yupi.Judge.codesandbox;

import cn.hutool.json.JSONUtil;
import com.yupi.Judge.CodeSandboxFactory;
import com.yupi.Judge.JudgeService;
import com.yupi.Judge.codesandbox.model.CodeSandBox;
import com.yupi.Judge.codesandbox.model.ExecuteRequest;
import com.yupi.Judge.codesandbox.model.ExecuteResponse;
import com.yupi.Judge.codesandbox.model.JudgeContext;
import com.yupi.common.ErrorCode;
import com.yupi.exception.BusinessException;
import com.yupi.manager.JudgeManager;
import com.yupi.model.entity.JudgeCase;
import com.yupi.model.entity.JudgeInfo;
import com.yupi.model.entity.Question;
import com.yupi.model.entity.QuestionSubmit;
import com.yupi.model.enums.QuestionSubmitStatusEnum;
import com.yupi.service.QuestionService;
import com.yupi.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

//判题业务流程
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Value("${codesandbox.type:example}") //默认example
    private String type;
    @Resource
    private QuestionService questionService;
    @Resource
    private JudgeManager judgeManager;
    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        //判断是否有提交记录
        QuestionSubmit submit = questionSubmitService.getById(questionSubmitId);
        if (submit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目提交不存在");
        }
        //判断题目是否存在
        Long questionId = submit.getQuestionId();
        Question byId = questionService.getById(questionId);
        if( byId == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        Integer status = submit.getStatus();
        //判断当前状态是不是判题中，是的话就不重复进行
        if(!status.equals(QuestionSubmitStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目正在判题");
        }
        //更改判题状态为running 判题中
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        questionSubmit.setId(questionSubmitId);
        boolean b = questionSubmitService.updateById(questionSubmit);
        if (!b){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"更改状态失败");
        }
        submit=questionSubmitService.getById(questionSubmitId);
        //调用代码沙箱
        CodeSandBox codeSandBox = CodeSandboxFactory.newInstance(type);
        codeSandBox=new CodeSandboxProxy(codeSandBox);
        String judgeCase = byId.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteRequest request=new ExecuteRequest();
        request.setLanguage(submit.getLanguage());
        request.setCode(submit.getCode());
        request.setInputList(inputList);

        ExecuteResponse executeResponse = codeSandBox.executeCode(request);
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeResponse.getOutputList());
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(byId);
        judgeContext.setQuestionSubmit(submit);
        //相当于对结果进行再校验
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6）修改数据库中的判题结果
        questionSubmit = new QuestionSubmit();
        questionSubmit.setId(questionSubmitId);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        boolean sub = questionSubmitService.updateById(questionSubmit);
        if (!sub){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"调用代码沙箱失败");
        }
        return questionSubmitService.getById(questionSubmitId);
    }
}
