package com.yupi.manager;

import com.yupi.Judge.codesandbox.DefaultLanguageJudgeStrategy;
import com.yupi.Judge.codesandbox.JavaLanguageJudgeStrategy;
import com.yupi.Judge.codesandbox.JudgeStratery;
import com.yupi.Judge.codesandbox.model.JudgeContext;
import com.yupi.model.entity.JudgeInfo;
import com.yupi.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

//判题策略体现
@Service
public class JudgeManager {
    //执行判题
    public JudgeInfo doJudge(JudgeContext context){
        QuestionSubmit questionSubmit=context.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStratery judgeStratery=new DefaultLanguageJudgeStrategy();
        if("java".equals(language)){
            judgeStratery=new JavaLanguageJudgeStrategy();
        }
        return judgeStratery.doJudge(context);
    }
}
