<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.URLDecoder" %>
<%
    // 读取"记住账号"Cookie，用于回填用户名
    String rememberedUser = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("rememberUser".equals(c.getName())) {
                rememberedUser = URLDecoder.decode(c.getValue(), "UTF-8");
                break;
            }
        }
    }
    // 获取登录失败时的错误信息
    String errorMsg = (String) request.getAttribute("errorMsg");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户登录</title>
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
            padding: 40px;
            width: 100%;
            max-width: 440px;
        }

        h1 {
            text-align: center;
            color: #333;
            font-size: 24px;
            margin-bottom: 8px;
        }

        .subtitle {
            text-align: center;
            color: #999;
            font-size: 13px;
            margin-bottom: 30px;
        }

        .error-box {
            background: #fff5f5;
            border: 1px solid #fed7d7;
            border-radius: 8px;
            padding: 12px 16px;
            color: #e74c3c;
            font-size: 14px;
            margin-bottom: 20px;
            text-align: center;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            font-size: 14px;
            color: #555;
            margin-bottom: 6px;
            font-weight: 600;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 12px 14px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 15px;
            transition: border-color 0.3s;
            outline: none;
        }

        input[type="text"]:focus,
        input[type="password"]:focus {
            border-color: #667eea;
        }

        .options-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            flex-wrap: wrap;
            gap: 10px;
        }

        .checkbox-label {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 14px;
            color: #666;
            cursor: pointer;
            font-weight: normal;
        }

        .checkbox-label input[type="checkbox"] {
            accent-color: #667eea;
            width: 16px;
            height: 16px;
        }

        .auto-login-group label.group-title {
            margin-bottom: 10px;
        }

        .auto-login-toggle {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 14px;
            color: #666;
            cursor: pointer;
            font-weight: normal;
        }

        .auto-login-toggle input[type="checkbox"] {
            accent-color: #667eea;
            width: 16px;
            height: 16px;
        }

        .auto-login-group {
            min-height: 70px;
        }

        .radio-group {
            display: none;
            flex-wrap: wrap;
            gap: 8px;
            margin-top: 10px;
        }

        .radio-group.show {
            display: flex;
        }

        .radio-label {
            display: flex;
            align-items: center;
            gap: 5px;
            font-size: 14px;
            color: #666;
            cursor: pointer;
            padding: 6px 14px;
            border: 2px solid #e0e0e0;
            border-radius: 20px;
            transition: all 0.2s;
            font-weight: normal;
        }

        .radio-label:hover {
            border-color: #667eea;
            background: #f0f0ff;
        }

        .radio-label input[type="radio"] {
            accent-color: #667eea;
        }

        .radio-label input[type="radio"]:checked + span {
            color: #667eea;
            font-weight: 600;
        }

        .btn-login {
            width: 100%;
            padding: 13px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: #fff;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 5px;
            transition: opacity 0.3s, transform 0.15s;
        }

        .btn-login:hover {
            opacity: 0.9;
            transform: translateY(-1px);
        }

        .btn-login:active {
            transform: translateY(0);
        }

        .hint {
            text-align: center;
            color: #999;
            font-size: 12px;
            margin-top: 16px;
            line-height: 1.6;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>用户登录</h1>
    <p class="subtitle">欢迎回来，请输入您的账号信息</p>

    <% if (errorMsg != null && !errorMsg.isEmpty()) { %>
    <div class="error-box"><%= errorMsg %></div>
    <% } %>

    <form action="LoginServlet" method="post">
        <!-- 用户名 -->
        <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" id="username" name="username"
                   placeholder="请输入用户名"
                   value="<%= rememberedUser %>" required>
        </div>

        <!-- 密码 -->
        <div class="form-group">
            <label for="password">密码</label>
            <input type="password" id="password" name="password"
                   placeholder="请输入密码" required>
        </div>

        <!-- 记住账号 -->
        <div class="options-row">
            <label class="checkbox-label">
                <input type="checkbox" name="rememberMe"
                       <%= (!rememberedUser.isEmpty()) ? "checked" : "" %>>
                记住账号
            </label>
        </div>

        <!-- 自动登录时间选择 -->
        <div class="form-group auto-login-group">
            <label class="auto-login-toggle">
                <input type="checkbox" id="autoLoginToggle" onchange="toggleAutoLogin()">
                自动登录
            </label>
            <input type="hidden" name="autoLogin" id="autoLoginValue" value="0">
            <div class="radio-group" id="autoLoginOptions">
                <label class="radio-label">
                    <input type="radio" name="autoLoginTime" value="604800" checked onchange="updateAutoLoginValue()">
                    <span>一周</span>
                </label>
                <label class="radio-label">
                    <input type="radio" name="autoLoginTime" value="2592000" onchange="updateAutoLoginValue()">
                    <span>一个月</span>
                </label>
                <label class="radio-label">
                    <input type="radio" name="autoLoginTime" value="7776000" onchange="updateAutoLoginValue()">
                    <span>三个月</span>
                </label>
            </div>
        </div>

        <button type="submit" class="btn-login">登 录</button>
        <p class="hint">
            测试账号：admin / 123456 &nbsp;或&nbsp; user / 654321
        </p>
    </form>
</div>
<script>
    function toggleAutoLogin() {
        var checkbox = document.getElementById('autoLoginToggle');
        var options = document.getElementById('autoLoginOptions');
        if (checkbox.checked) {
            options.classList.add('show');
            updateAutoLoginValue();
        } else {
            options.classList.remove('show');
            document.getElementById('autoLoginValue').value = '0';
        }
    }

    function updateAutoLoginValue() {
        var radios = document.getElementsByName('autoLoginTime');
        for (var i = 0; i < radios.length; i++) {
            if (radios[i].checked) {
                document.getElementById('autoLoginValue').value = radios[i].value;
                break;
            }
        }
    }
</script>
</body>
</html>
