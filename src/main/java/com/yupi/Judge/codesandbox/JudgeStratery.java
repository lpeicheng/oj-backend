package com.yupi.Judge.codesandbox;

import com.yupi.Judge.codesandbox.model.JudgeContext;
import com.yupi.model.entity.JudgeInfo;

//策略模式，根据语言的不同选择对应的判题条件
public interface JudgeStratery {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
