package com.yupi.Judge.codesandbox;

import cn.hutool.json.JSONUtil;
import com.yupi.Judge.codesandbox.model.JudgeContext;
import com.yupi.model.entity.JudgeCase;
import com.yupi.model.entity.JudgeConfig;
import com.yupi.model.entity.JudgeInfo;
import com.yupi.model.entity.Question;
import com.yupi.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.Optional;

public class JavaLanguageJudgeStrategy implements JudgeStratery{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
            JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
            List<String> inputList = judgeContext.getInputList();
            List<String> outputList = judgeContext.getOutputList();
            List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
            Question question = judgeContext.getQuestion();
            JudgeInfo judgeInfo1=new JudgeInfo();
            //判断输入和输出个数是否相同
            JudgeInfoMessageEnum judgeInfoMessageEnum=JudgeInfoMessageEnum.ACCEPTED;
            if(inputList.size() != outputList.size()){
                judgeInfoMessageEnum=JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfo1.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfo1;
            }
            //逐个判断输入用例的结果是否和输出结果一致
            for (int i = 0; i < judgeCaseList.size(); i++) {
                JudgeCase judgeCase = judgeCaseList.get(i);
                if(!judgeCase.getOutput().equals(outputList.get(i))){
                    judgeInfoMessageEnum=JudgeInfoMessageEnum.WRONG_ANSWER;
                    judgeInfo1.setMessage(judgeInfoMessageEnum.getValue());
                    return judgeInfo1;
                }
            }
            // TODO 后期要解决获取不了内存问题
            Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
            Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
            JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
            //判断内存和时间限制 java程序需要额外执行5秒
            if(memory>judgeConfig.getMemoryLimit()){
                judgeInfoMessageEnum=JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
                judgeInfo1.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfo1;
            }
            if((time-5000L)>judgeConfig.getTimeLimit()){
                judgeInfoMessageEnum=JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
                judgeInfo1.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfo1;
            }
            judgeInfo1.setTime(time);
            judgeInfo1.setMemory(memory);
            judgeInfo1.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfo1;
        }
}
