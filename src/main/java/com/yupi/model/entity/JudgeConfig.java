package com.yupi.model.entity;

import lombok.Data;

//判题配置
@Data
public class JudgeConfig {
    private Long memoryLimit;
    private Long timeLimit;
    private Long stackLimit;
}
