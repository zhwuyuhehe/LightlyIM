# LightlyIM

LightlyIM是一个学习构建的轻量化在线聊天系统前端，支持 WebSocket 实时通信、历史消息滚动加载、用户在线状态展示等功能。

> ⚡ 使用Vue3 + ElementPlus +Vite 搭配 Spring Boot 3 + WebFlux + Redis + RabbitMQ 实现完整后端功能

---

## 🌟 项目特性

1. Vue 3 Composition API + Pinia 状态管理
2. Element Plus UI 组件库
3. WebSocket 实时聊天
4. 带加载动画的历史消息滚动加载
5. 自动滚动至最新消息
6. 支持用户在线状态列表
7. 使用 Redis 缓存聊天记录，定期持久化
8. 使用RabbiMQ处理高并发消息
9. 使用ESLint规范化前端代码

---

## 技术栈

| 技术                                                                      | 说明            |
|-------------------------------------------------------------------------|---------------|
| [Vue 3](https://cn.vuejs.org/)                                          | 渐进式前端框架       |
| [Element Plus](https://element-plus.org/)                               | 基于 Vue 3 的组件库 |
| [Vite](https://vitejs.dev/)                                             | 极速构建工具        |
| [Pinia](https://pinia.vuejs.org/)                                       | Vue 的状态管理     |
| [TypeScript](https://www.typescriptlang.org/)                           | 强类型支持         |
| [WebSocket](https://developer.mozilla.org/zh-CN/docs/Web/API/WebSocket) | 实时通信          |

---

## 接口地址说明

1. > 系统后端接口 8080
2. > RabbiMQ设置为rabbitmq:host:
   >
   >localhost;
   port: 5672;
   username: rabbitRoot;
   password: rabbitRoot;

3. > Redis接口
   > redis:
   >
   > port: 6379;
   > database: 0;
   > host: localhost ;
   >
   > 并使用了**lettuce**
   > 设置为
   > pool:
   > enabled: true
   > max-active: 20
   > max-idle: 10

4. > Spring-Session设置为spring:session:
   > **redis:**
   > repository-type: indexed;
   > save-mode: always;
   > namespace: LightlyIM;
   > timeout: 5m

## 等待补充说明...

