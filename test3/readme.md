# 作业3：用户登录系统

基于 Servlet + JSP + Cookie + Session + Filter 实现的用户登录系统。

---

## 功能列表

| 功能 | 说明 |
|------|------|
| 用户登录 | 输入用户名和密码进行登录验证 |
| 记住账号 | 勾选后下次访问登录页自动回填用户名 |
| 自动登录 | 选择时间后，关闭浏览器再次访问可免登录 |
| 有效信息检测 | 前端 + 后端双重校验用户名和密码是否为空 |
| 中文乱码过滤器 | 统一设置 UTF-8 编码，解决中文乱码 |
| 用户注销 | 清除 Session 和自动登录 Cookie |

---

## 测试账号

| 用户名 | 密码 |
|--------|------|
| admin  | 123456 |
| user   | 654321 |

---

## 目录结构

```
test3/
├── src/
│   └── com/
│       └── login/
│           ├── bean/
│           │   └── User.java            ← 用户实体类
│           ├── servlet/
│           │   ├── LoginServlet.java     ← 登录处理Servlet
│           │   └── LogoutServlet.java    ← 注销处理Servlet
│           └── filter/
│               ├── EncodingFilter.java   ← 中文乱码过滤器
│               └── AutoLoginFilter.java  ← 自动登录过滤器
├── web/
│   ├── login.jsp                        ← 登录页面
│   ├── index.jsp                        ← 首页（显示登录状态）
│   └── WEB-INF/
│       └── web.xml                      ← 部署描述符
└── readme.md                            ← 本文件
```

---

## 技术栈

- Java Servlet 4.0（`@WebServlet` / `@WebFilter` 注解）
- JSP（Java Server Pages）
- Cookie（记住账号 + 自动登录）
- Session（登录状态管理）
- Filter（编码过滤 + 自动登录过滤）

---

## 页面说明

### login.jsp — 登录页面

- 用户名输入框（支持记住账号自动回填）
- 密码输入框
- "记住账号"复选框
- 自动登录时间单选（不自动登录 / 一周 / 一个月 / 三个月）
- 登录失败时显示红色错误提示

### index.jsp — 首页

- **未登录**：显示提示文字和"去登录"超链接
- **已登录**：显示用户名、登录状态、Session ID，以及"注销"超链接

---

## 核心流程

### 登录流程
1. 用户访问 `login.jsp`，填写用户名和密码
2. 提交表单 POST 到 `LoginServlet`
3. `LoginServlet` 验证用户名密码
   - 成功：存入 Session，设置自动登录 Cookie，重定向到 `index.jsp`
   - 失败：设置错误信息，转发回 `login.jsp`

### 自动登录流程
1. `AutoLoginFilter` 拦截所有请求
2. 检查 Session 中是否已有用户 → 有则直接放行
3. 检查 Cookie 中是否有 `autoLogin` → 有则解析并验证
4. 验证通过则自动存入 Session，实现免登录

### 注销流程
1. 用户点击"注销"链接，请求 `LogoutServlet`
2. 清除 Session 中的 User 对象
3. 删除自动登录 Cookie（设置 maxAge=0）
4. 重定向到 `index.jsp`

---

## 部署方式

1. 在 IntelliJ IDEA 中配置 Tomcat（8.5+）
2. 将 `web` 目录设为 Web 应用根目录
3. 将 `src` 目录设为源代码根目录
4. 部署并启动 Tomcat
5. 访问 `http://localhost:8080/test3/` 即可看到首页
