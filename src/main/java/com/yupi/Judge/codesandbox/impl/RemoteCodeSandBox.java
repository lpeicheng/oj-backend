package com.yupi.Judge.codesandbox.impl;
import java.util.List;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.Judge.codesandbox.model.CodeSandBox;
import com.yupi.Judge.codesandbox.model.ExecuteRequest;
import com.yupi.Judge.codesandbox.model.ExecuteResponse;
import com.yupi.common.ErrorCode;
import com.yupi.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;


public class RemoteCodeSandBox implements CodeSandBox {
    //用于在执行判题业务前进行鉴权，防止浪费虚拟机资源
    //TODO 优化点使用Sa-Token进行更好的权限校验
    private static final String SECRET_KEY="secretKey";
    private static final String SECRET_HEADER="secretHeader";
    @Override
    public ExecuteResponse executeCode(ExecuteRequest request) {
         System.out.println("远程代码沙箱");
         String str = JSONUtil.toJsonStr(request);
         String url="http://localhost:8090/execute";
         String response= HttpUtil.createPost(url)
                 .header(SECRET_HEADER,SECRET_KEY)
                 .body(str)
                 .execute()
                 .body();
         if(StringUtils.isBlank(response)){
             throw new BusinessException(ErrorCode.OPERATION_ERROR,"远程代码沙箱执行失败");
         }
         return JSONUtil.toBean(response,ExecuteResponse.class);
        }
}
