package com.yupi.model.dto.questionSubmit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class QuestionSubmitAddRequest {

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 判题信息
     */
    private String judgeInfo;

    /**
     * 提交语言
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;
}
