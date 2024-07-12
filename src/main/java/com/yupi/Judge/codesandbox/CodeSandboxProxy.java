package com.yupi.Judge.codesandbox;

import com.yupi.Judge.codesandbox.model.CodeSandBox;
import com.yupi.Judge.codesandbox.model.ExecuteRequest;
import com.yupi.Judge.codesandbox.model.ExecuteResponse;
import lombok.extern.slf4j.Slf4j;

//每次在代码执行前后输出信息
@Slf4j
public class CodeSandboxProxy implements CodeSandBox {
    private final CodeSandBox codeSandbox;


    public CodeSandboxProxy(CodeSandBox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }
    @Override
    public ExecuteResponse executeCode(ExecuteRequest request) {
        log.info("代码请求信息"+request);
        ExecuteResponse response = codeSandbox.executeCode(request);
        log.info("代码执行结果"+response);
        return response;
    }
}
