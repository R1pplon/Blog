# 项目

## 后端

## 模型

### User

```
ID
name            名称
email           邮箱
password        密码
avatar_url      头像
create_time     创建时间
update_time     修改时间
status          状态（0为禁用，1为正常）
role            角色（0为管理员，1为普通用户）
```

一个用户可以有多篇文章

一个用户可以有多个评论

### Article

```
id              ID
title           标题
author_id       作者ID
create_time     创建时间
update_time     修改时间
```

一个文章可以有多个评论

一个文章只有一个用户

### Comment

```
id              ID
user_id         用户ID
article_id      文章ID
content         内容
parent_id       父评论ID（没有则是一级评论，有则是二级评论）
create_time     创建时间
update_time     修改时间
status          状态状态（0:待审核 1:正常）
```

一个评论只有一个用户

一个评论对应一个文章

## 接口

### 统一响应格式

```json
{
    "code": 200,
    "message": "success",
    "data": {}
}
```

### 公共接口（游客可访问）

```txt
# 用户相关
POST /api/public/user/register     # 用户注册
POST /api/public/user/login        # 用户登录

# 文章浏览
GET /api/public/articles           # 获取文章列表
GET /api/public/article/{id}       # 获取文章详情

# 评论浏览
GET /api/public/article/{id}/comments # 获取文章评论
```

### 用户接口（需登录，普通用户和管理员都可访问）

```txt
# 用户信息
GET /api/user/info                 # 获取当前用户信息
PUT /api/user/info                 # 更新当前用户信息

# 评论操作
POST /api/user/article/{id}/comment # 发表评论
GET /api/user/comments              # 获取当前用户的评论
PUT /api/user/comment/{id}          # 更新当前用户的评论
DELETE /api/user/comment/{id}       # 删除当前用户的评论
```

### 管理员专用接口

```txt
# 文章管理
POST /api/admin/article            # 创建文章
PUT /api/admin/article/{id}        # 更新文章
DELETE /api/admin/article/{id}     # 删除文章

# 用户管理
GET /api/admin/users               # 获取所有用户
GET /api/admin/user/{id}           # 获取指定用户
PUT /api/admin/user/{id}           # 更新用户信息
DELETE /api/admin/user/{id}        # 删除用户

# 评论管理
GET /api/admin/comments            # 获取所有评论
PUT /api/admin/comment/{id}        # 更新任意评论
DELETE /api/admin/comment/{id}     # 删除任意评论
GET /api/admin/comment/approve     # 获取待审核评论
PUT /api/admin/comment/{id}/status # 审核评论状态
```


