package com.yupi.Judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteRequest {
    //输入用例
    private List<String> inputList;
    //代码
    private String code;
    private String language;
}
