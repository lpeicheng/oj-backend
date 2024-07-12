# OJ 判题项目

> 作者：paopao https://github.com/lpeicheng/oj-backend)

[toc]

### 主流框架 & 特性

- Spring Boot 2.7.2
- Spring MVC
- MyBatis + MyBatis Plus 数据访问（开启分页）
- Spring Boot 调试工具和项目处理器
- Spring AOP 切面编程
- Spring Scheduler 定时任务
- Spring 事务注解

### 数据存储

- MySQL 数据库
- Redis 内存数据库

### 工具类

- Hutool 工具库
- Apache Commons Lang3 工具类
- Lombok 注解

### 业务特性

- Spring Session Redis 分布式登录
- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- Swagger + Knife4j 接口文档
- 自定义权限注解 + 全局校验
- 全局跨域处理
- 长整数丢失精度解决
- 多环境配置


## 业务功能

- 题目模块
    - 创建题目（管理员）
    - 删除题目（管理员）
    - 修改题目（管理员）
    - 搜索题目（用户）
    - 在线做题
    - 提交题目代码
- 用户模块
    - 注册
    - 登录
- 判题模块
    - 提交判题（结果是否正确与错误）
    - 错误处理（内存溢出、安全性、超时）
    - 自主实现 代码沙箱（安全沙箱）
    - 开放接口（提供一个独立的新服务）
