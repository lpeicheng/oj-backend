package com.yupi.Judge.codesandbox.model;

import com.yupi.model.entity.JudgeCase;
import com.yupi.model.entity.JudgeInfo;
import com.yupi.model.entity.Question;
import com.yupi.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

//判题过程传递参数的上下文
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;
    private List<String> inputList;
    private List<String> outputList;
    private List<JudgeCase> judgeCaseList;
    private Question question;
    private QuestionSubmit questionSubmit;
}
