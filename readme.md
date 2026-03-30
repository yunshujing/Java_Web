# 学生成绩录入系统

一个基于 **Servlet + HTML** 的单页面成绩录入与处理系统。

---

## 项目概述

本项目实现了一个完整的"前端录入 → 服务端处理 → 结果展示"流程：
- 学生通过 HTML 表单录入个人信息和三门课程成绩
- 服务端 Servlet 接收数据后进行合法性校验、成绩计算和等级评定
- 将处理结果以美观的页面形式展示给用户

---

## 项目结构

```
Java_Web/
├── src/
│   └── com/score/
│       └── ScoreServlet.java    ← 核心Servlet，处理成绩数据
├── web/
│   ├── WEB-INF/
│   │   └── web.xml              ← Web应用配置文件
│   └── score.html               ← 前端成绩录入页面
└── readme.md                    ← 本说明文件
```

---

## 功能说明

### 1. 前端页面（score.html）

**表单字段：**

| 字段 | 类型 | 说明 |
|------|------|------|
| 学号 | 文本输入 | 学生唯一标识，如 `2024001` |
| 姓名 | 文本输入 | 学生姓名 |
| 兴趣爱好 | 多选复选框 | 可选：阅读、运动、音乐、编程、绘画、旅行 |
| Java成绩 | 数字输入 | 范围 0~100 |
| Web前端成绩 | 数字输入 | 范围 0~100 |
| Web服务端成绩 | 数字输入 | 范围 0~100 |

**提交方式：** `POST` 提交到 `/ScoreServlet`

### 2. 服务端处理（ScoreServlet）

Servlet 接收表单数据后，完成以下处理：

#### a) 数据合法性校验
- 学号、姓名不能为空
- 三门成绩必须为数字，范围 0~100
- 校验不通过时输出错误提示页面，并提供"返回重新填写"按钮

#### b) 成绩计算
- **总分** = Java + Web前端 + Web服务端
- **平均分** = 总分 / 3

#### c) 等级评定（基于平均分）

| 平均分范围 | 等级 |
|-----------|------|
| ≥ 90 | 优秀 |
| ≥ 80 | 良好 |
| ≥ 70 | 中等 |
| ≥ 60 | 及格 |
| < 60 | 不及格 |

#### d) 附加功能
- 每门课程单独评价（优秀/良好/中等/及格/不及格）
- 找出最强科目和薄弱科目
- 结果页面与录入页面风格统一

---

## 部署方式

### 前提条件
- JDK 8 或以上
- Apache Tomcat 8.5 或以上（支持 Servlet 3.1+）
- IntelliJ IDEA（推荐）

### IntelliJ IDEA 部署步骤

1. **打开项目**：用 IDEA 打开 `Java_Web` 文件夹
2. **配置项目结构**（File → Project Structure）：
   - **Modules** → 将 `src` 标记为 Sources Root
   - **Modules** → 将 `web` 标记为 Web Resource Directory
   - **Facets** → 添加 Web facet，Deployment Descriptor 指向 `web/WEB-INF/web.xml`
3. **添加 Servlet 依赖**：
   - Project Structure → Libraries → 添加 Tomcat 的 `servlet-api.jar`
   - 或者在 Modules → Dependencies 中添加 Tomcat 库
4. **配置 Tomcat**：
   - Run → Edit Configurations → 添加 Tomcat Server → Local
   - Deployment 标签页 → 添加 Artifact
5. **运行**：点击运行按钮，浏览器自动打开 `score.html`

---

## 技术要点

- 使用 `@WebServlet` 注解配置 Servlet 映射（免 XML 配置）
- `request.setCharacterEncoding("UTF-8")` 解决中文乱码
- `response.setContentType("text/html;charset=UTF-8")` 确保输出中文正常
- 前端 HTML5 表单自带基础校验（`required`、`min`/`max`）
- 服务端双重校验保证数据安全性
