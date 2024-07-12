package com.yupi.model.dto.question;

import com.yupi.model.entity.JudgeCase;
import com.yupi.model.entity.JudgeConfig;
import lombok.Data;

import java.util.List;


@Data
public class QuestionAddRequest {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

}
