# 数据库初始化

-- 创建库
create database if not exists oj;

-- 切换库
use oj;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    email varchar(128)                        null comment '邮箱',
    userState varchar(128) default '正常' not null comment '账号状态 正常/封号',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (id)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 题目表
create table if not exists question
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    answer text                               null comment '答案',
    judgeCase text                               null comment '判题用例',
    judgeConfig text                              null comment '判题配置',
    acceptedNum int default 0 not null comment '通过人数',
    submitNum int default 0 not null comment '提交人数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 用户提交
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    questionId     bigint                             not null comment '题目id',
    userId     bigint                             not null comment '提交用户 id',
    judgeInfo text                               null comment '判题信息',
    language varchar(128) default 'java' not null comment '提交语言',
    code text                               null comment '提交代码',
    status tinyint default '0' not null comment '判题状态 0：未提交 1：判题中 2：判题完成  3：失败',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '用户提交表';
