package com.yupi.Judge.codesandbox.model;

import com.yupi.model.entity.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteResponse {
    private List<String> outputList;
    private JudgeInfo judgeInfo;
    private Integer status;
    //接口信息
    private String message;
}
