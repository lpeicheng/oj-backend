package com.yupi.Judge.codesandbox.impl;

import com.yupi.Judge.codesandbox.model.CodeSandBox;
import com.yupi.Judge.codesandbox.model.ExecuteRequest;
import com.yupi.Judge.codesandbox.model.ExecuteResponse;
import com.yupi.model.entity.JudgeInfo;
import com.yupi.model.enums.JudgeInfoMessageEnum;
import com.yupi.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteResponse executeCode(ExecuteRequest request) {
        List<String> inputList = request.getInputList();
        ExecuteResponse executeResponse=new ExecuteResponse();
        executeResponse.setOutputList(inputList);
        executeResponse.setMessage("执行成功");
        executeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo=new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        executeResponse.setJudgeInfo(judgeInfo);
        return executeResponse;
    }
}
