-- 测试数据SQL脚本
-- 基于 User, Article, Comment 模型生成的测试数据
create database blog_db;
-- 插入用户数据 (users表)
INSERT INTO users (username, email, password, avatar_url, create_time, update_time, status, role) VALUES
-- 管理员用户
('admin', 'admin@example.com', '$2a$10$IpnwiApLvNXEjUqjJJB5suFYB4Ql4LN.RSIpoCIorPCfn2TjIYH4W', '/uploads/avatars/admin.jpg', '2024-01-15 10:00:00', '2024-01-15 10:00:00', 1, 0),

-- 普通用户
('张三', 'zhangsan@example.com', '$2a$10$IpnwiApLvNXEjUqjJJB5suFYB4Ql4LN.RSIpoCIorPCfn2TjIYH4W', NULL, '2024-01-16 09:30:00', '2024-01-16 09:30:00', 1, 1),
('李四', 'lisi@example.com', '$2a$10$IpnwiApLvNXEjUqjJJB5suFYB4Ql4LN.RSIpoCIorPCfn2TjIYH4W', NULL, '2024-01-17 14:20:00', '2024-01-17 14:20:00', 1, 1),
('王五', 'wangwu@example.com', '$2a$10$IpnwiApLvNXEjUqjJJB5suFYB4Ql4LN.RSIpoCIorPCfn2TjIYH4W', NULL, '2024-01-18 16:45:00', '2024-01-18 16:45:00', 1, 1),
('赵六', 'zhaoliu@example.com', '$2a$10$IpnwiApLvNXEjUqjJJB5suFYB4Ql4LN.RSIpoCIorPCfn2TjIYH4W', NULL, '2024-01-19 11:15:00', '2024-01-19 11:15:00', 1, 1),
('孙七', 'sunqi@example.com', '$2a$10$IpnwiApLvNXEjUqjJJB5suFYB4Ql4LN.RSIpoCIorPCfn2TjIYH4W', NULL, '2024-01-20 13:30:00', '2024-01-20 13:30:00', 1, 1);

-- 插入文章数据 (articles表)
INSERT INTO articles (title, content, create_time, update_time, author_id) VALUES
-- 管理员发布的文章
('Spring Boot 入门指南', 
'# Spring Boot 入门指南

## 什么是Spring Boot？

Spring Boot是一个开源的Java框架，用于快速构建企业级应用程序。

## 主要特性

- **自动配置**：根据classpath自动配置Spring应用
- **起步依赖**：简化Maven/Gradle配置
- **内嵌服务器**：支持Tomcat、Jetty等

## 快速开始

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 创建第一个Controller

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }
}
```

## 总结

Spring Boot大大简化了Spring应用的开发和部署过程，是现代Java开发的首选框架。',
'2024-01-21 10:00:00', '2024-01-21 10:00:00', 1),

('Vue.js 组件化开发实践', 
'# Vue.js 组件化开发实践

## 前言

Vue.js是一个渐进式JavaScript框架，组件化是其核心思想之一。

## 组件基础

### 创建组件

```vue
<template>
  <div class="user-card">
    <h3>{{ user.name }}</h3>
    <p>{{ user.email }}</p>
  </div>
</template>

<script setup>
import { defineProps } from "vue"

const props = defineProps({
  user: {
    type: Object,
    required: true
  }
})
</script>
```

### 组件通信

- **父传子**：props
- **子传父**：emit
- **兄弟组件**：事件总线或状态管理

## 最佳实践

1. 保持组件单一职责
2. 合理使用插槽(slot)
3. 利用组合式API提高代码复用性

## 结语

掌握组件化开发是成为Vue.js高手的必经之路！',
'2024-01-22 14:30:00', '2024-01-22 14:30:00', 1),

-- 用户发布的文章
('MySQL数据库优化技巧', 
'# MySQL数据库优化技巧

## 索引优化

### 1. 选择合适的索引类型

- **B-Tree索引**：最常用的索引类型
- **哈希索引**：适用于等值查询
- **全文索引**：用于文本搜索

### 2. 索引设计原则

```sql
-- 为经常查询的字段创建索引
CREATE INDEX idx_user_email ON users(email);

-- 复合索引的顺序很重要
CREATE INDEX idx_user_status_create_time ON users(status, create_time);
```

## 查询优化

### 使用EXPLAIN分析查询

```sql
EXPLAIN SELECT * FROM users WHERE email = "test@example.com";
```

### 避免全表扫描

- 合理使用WHERE条件
- 避免在WHERE子句中使用函数
- 利用LIMIT限制结果集

## 总结

数据库优化是一个持续的过程，需要根据具体业务场景进行调整。',
'2024-01-23 09:15:00', '2024-01-23 09:15:00', 2),

('JavaScript异步编程详解', 
'# JavaScript异步编程详解

## 异步编程的重要性

JavaScript是单线程语言，异步编程是处理耗时操作的关键。

## 发展历程

### 1. 回调函数时代

```javascript
function getData(callback) {
    setTimeout(() => {
        callback("数据获取成功");
    }, 1000);
}

getData((result) => {
    console.log(result);
});
```

### 2. Promise时代

```javascript
function getData() {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve("数据获取成功");
        }, 1000);
    });
}

getData().then(result => {
    console.log(result);
});
```

### 3. Async/Await时代

```javascript
async function getData() {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve("数据获取成功");
        }, 1000);
    });
}

async function main() {
    const result = await getData();
    console.log(result);
}
```

## 实际应用

异步编程在Web开发中无处不在：
- AJAX请求
- 文件读写
- 定时器操作

掌握异步编程是现代前端开发的必备技能！',
'2024-01-24 16:20:00', '2024-01-24 16:20:00', 3),

('Docker容器化部署指南', 
'# Docker容器化部署指南

## Docker简介

Docker是一个开源的容器化平台，让应用程序的部署变得简单高效。

## 基础概念

- **镜像(Image)**：应用程序的模板
- **容器(Container)**：镜像的运行实例
- **仓库(Repository)**：存储镜像的地方

## Dockerfile示例

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 常用命令

```bash
# 构建镜像
docker build -t my-app .

# 运行容器
docker run -d -p 8080:8080 my-app

# 查看容器状态
docker ps

# 停止容器
docker stop container_id
```

## Docker Compose

对于多服务应用，推荐使用Docker Compose：

```yaml
version: "3.8"
services:
  app:
    build: .
    ports:
      - "8080:8080"
  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
```

Docker让部署变得前所未有的简单！',
'2024-01-25 11:30:00', '2024-01-25 11:30:00', 4),

('React Hooks 深入理解', 
'# React Hooks 深入理解

## Hooks的诞生背景

React Hooks解决了类组件的诸多问题，让函数组件也能拥有状态。

## 常用Hooks详解

### useState

```jsx
import { useState } from "react";

function Counter() {
    const [count, setCount] = useState(0);
    
    return (
        <div>
            <p>Count: {count}</p>
            <button onClick={() => setCount(count + 1)}>
                增加
            </button>
        </div>
    );
}
```

### useEffect

```jsx
import { useEffect, useState } from "react";

function UserProfile({ userId }) {
    const [user, setUser] = useState(null);
    
    useEffect(() => {
        fetchUser(userId).then(setUser);
    }, [userId]);
    
    return user ? <div>{user.name}</div> : <div>Loading...</div>;
}
```

### 自定义Hooks

```jsx
function useLocalStorage(key, initialValue) {
    const [value, setValue] = useState(() => {
        return localStorage.getItem(key) || initialValue;
    });
    
    useEffect(() => {
        localStorage.setItem(key, value);
    }, [key, value]);
    
    return [value, setValue];
}
```

## 最佳实践

1. 遵循Hooks使用规则
2. 合理拆分自定义Hooks
3. 正确处理依赖数组

Hooks让React开发更加简洁优雅！',
'2024-01-26 15:45:00', '2024-01-26 15:45:00', 5);

-- 插入评论数据 (comments表)
INSERT INTO comments (content, create_time, status, user_id, article_id, parent_id) VALUES
-- 文章1的评论
('这篇Spring Boot教程写得很详细，对新手很友好！', '2024-01-21 11:30:00', 1, 2, 1, NULL),
('确实，我也是通过这种方式入门的Spring Boot', '2024-01-21 12:15:00', 1, 3, 1, NULL),
('有没有更高级的内容？比如微服务架构', '2024-01-21 14:20:00', 0, 4, 1, NULL),
('@王五 后续会有微服务系列文章，敬请期待', '2024-01-21 15:30:00', 1, 1, 1, 3),

-- 文章2的评论  
('Vue.js的组件化思想确实很棒，学到了很多', '2024-01-22 16:00:00', 1, 3, 2, NULL),
('Composition API比Options API更灵活', '2024-01-22 17:30:00', 1, 4, 2, NULL),
('能否分享一些大型项目的组件设计经验？', '2024-01-22 18:45:00', 1, 5, 2, NULL),

-- 文章3的评论
('数据库优化确实很重要，索引设计需要经验积累', '2024-01-23 10:30:00', 1, 4, 3, NULL),
('EXPLAIN命令是数据库调优的利器', '2024-01-23 11:45:00', 1, 5, 3, NULL),
('还有哪些优化技巧可以分享？', '2024-01-23 13:20:00', 0, 6, 3, NULL),
('@孙七 可以关注查询缓存和分表分库策略', '2024-01-23 14:10:00', 1, 2, 3, 10),

-- 文章4的评论
('异步编程从callback到async/await的演进很清晰', '2024-01-24 18:30:00', 1, 5, 4, NULL),
('Promise.all()在并发场景下很有用', '2024-01-24 19:15:00', 1, 6, 4, NULL),
('希望能看到更多实际项目中的应用案例', '2024-01-24 20:00:00', 1, 2, 4, NULL),

-- 文章5的评论
('Docker真的是运维神器，解决了很多环境问题', '2024-01-25 13:00:00', 1, 6, 5, NULL),
('Kubernetes是下一步要学习的', '2024-01-25 14:30:00', 0, 2, 5, NULL),
('容器化确实让部署变得简单多了', '2024-01-25 16:20:00', 1, 3, 5, NULL),

-- 文章6的评论
('Hooks让React代码更加简洁', '2024-01-26 17:00:00', 1, 2, 6, NULL),
('自定义Hooks的复用性很强', '2024-01-26 18:30:00', 1, 3, 6, NULL),
('useEffect的依赖数组确实需要注意', '2024-01-26 19:45:00', 1, 4, 6, NULL),
('@王五 是的，依赖数组的处理是常见的坑', '2024-01-26 20:15:00', 1, 5, 6, 19);

-- 验证数据插入结果的查询语句
-- SELECT COUNT(*) as total_users FROM users;
-- SELECT COUNT(*) as total_articles FROM articles;  
-- SELECT COUNT(*) as total_comments FROM comments;

-- 查看文章及其作者信息
-- SELECT a.title, u.username as author, a.create_time 
-- FROM articles a 
-- JOIN users u ON a.author_id = u.id 
-- ORDER BY a.create_time;

-- 查看评论及相关信息
-- SELECT c.content, u.username as commenter, a.title as article_title, c.create_time
-- FROM comments c
-- JOIN users u ON c.user_id = u.id  
-- JOIN articles a ON c.article_id = a.id
-- ORDER BY c.create_time; 