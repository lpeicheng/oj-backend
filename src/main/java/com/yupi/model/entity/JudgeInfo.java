package com.yupi.model.entity;

import lombok.Data;

//判题信息
@Data
public class JudgeInfo {
    private String message;
    private Long time;
    private Long memory;
}
