<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.login.bean.User" %>
<%
    // 从Session中获取用户信息
    User user = (User) session.getAttribute("user");
    boolean isLoggedIn = (user != null);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>首页</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: "Microsoft YaHei", "PingFang SC", sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .container {
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
            padding: 50px 40px;
            width: 100%;
            max-width: 480px;
            text-align: center;
        }

        .avatar {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            margin: 0 auto 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 36px;
            color: #fff;
        }

        .avatar-logged-in {
            background: linear-gradient(135deg, #27ae60, #2ecc71);
        }

        .avatar-guest {
            background: linear-gradient(135deg, #95a5a6, #bdc3c7);
        }

        h1 {
            color: #333;
            font-size: 22px;
            margin-bottom: 10px;
        }

        .welcome-text {
            color: #888;
            font-size: 14px;
            margin-bottom: 30px;
            line-height: 1.6;
        }

        .username-display {
            display: inline-block;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: #fff;
            padding: 6px 20px;
            border-radius: 20px;
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 30px;
        }

        .info-card {
            background: #f8f9fc;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 24px;
            text-align: left;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            font-size: 14px;
            border-bottom: 1px solid #eee;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            color: #888;
        }

        .info-value {
            color: #333;
            font-weight: 600;
        }

        .status-online {
            color: #27ae60;
        }

        .btn {
            display: inline-block;
            padding: 12px 36px;
            border-radius: 8px;
            font-size: 15px;
            font-weight: 600;
            text-decoration: none;
            transition: opacity 0.3s, transform 0.15s;
        }

        .btn:hover {
            opacity: 0.9;
            transform: translateY(-1px);
        }

        .btn-login {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: #fff;
        }

        .btn-logout {
            background: linear-gradient(135deg, #e74c3c, #c0392b);
            color: #fff;
        }
    </style>
</head>
<body>
<div class="container">
    <% if (isLoggedIn) { %>
        <!-- 已登录状态 -->
        <div class="avatar avatar-logged-in">&#10004;</div>
        <h1>欢迎回来！</h1>
        <p class="welcome-text">您已成功登录系统</p>
        <div class="username-display"><%= user.getUsername() %></div>
        <div class="info-card">
            <div class="info-row">
                <span class="info-label">用户名</span>
                <span class="info-value"><%= user.getUsername() %></span>
            </div>
            <div class="info-row">
                <span class="info-label">登录状态</span>
                <span class="info-value status-online">● 在线</span>
            </div>
            <div class="info-row">
                <span class="info-label">Session ID</span>
                <span class="info-value" style="font-size:12px;word-break:break-all">
                    <%= session.getId().substring(0, 16) %>...
                </span>
            </div>
        </div>
        <a href="LogoutServlet" class="btn btn-logout">注 销</a>
    <% } else { %>
        <!-- 未登录状态 -->
        <div class="avatar avatar-guest">&#128100;</div>
        <h1>欢迎访问</h1>
        <p class="welcome-text">您尚未登录，请先登录后查看更多内容</p>
        <a href="login.jsp" class="btn btn-login">去 登 录</a>
    <% } %>
</div>
</body>
</html>
