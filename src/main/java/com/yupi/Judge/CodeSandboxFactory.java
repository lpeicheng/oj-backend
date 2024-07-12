package com.yupi.Judge;

import com.yupi.Judge.codesandbox.impl.ExampleCodeSandBox;
import com.yupi.Judge.codesandbox.impl.RemoteCodeSandBox;
import com.yupi.Judge.codesandbox.model.CodeSandBox;
import org.springframework.beans.factory.annotation.Value;

//代码沙箱工厂模式（根据配置文件定义的属性选择不同的代码沙箱）
public class CodeSandboxFactory{
    public static CodeSandBox newInstance(String type){
        switch (type){
            case "example":
                return new ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            default: return new ExampleCodeSandBox();
        }
    }
}
