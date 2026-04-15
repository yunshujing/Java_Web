package com.login.servlet;

import com.login.bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 登录处理Servlet
 * 功能：验证用户名和密码 → 设置Session和自动登录Cookie → 跳转
 *
 * 合法用户（硬编码演示）：
 *   用户名: admin  密码: 123456
 *   用户名: user   密码: 654321
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取表单参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String autoLogin = request.getParameter("autoLogin");
        String rememberMe = request.getParameter("rememberMe");

        // ---------- 有效信息检测 ----------
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMsg", "用户名和密码不能为空！");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        username = username.trim();
        password = password.trim();

        // ---------- 验证用户名和密码 ----------
        if (validateUser(username, password)) {
            // 登录成功：将用户信息存入Session
            User user = new User(username, password);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // ---------- 处理"记住账号"功能 ----------
            if ("on".equals(rememberMe)) {
                Cookie usernameCookie = new Cookie("rememberUser", URLEncoder.encode(username, "UTF-8"));
                usernameCookie.setMaxAge(60 * 60 * 24 * 30); // 记住30天
                usernameCookie.setPath(request.getContextPath());
                response.addCookie(usernameCookie);
            } else {
                // 取消记住：删除Cookie
                Cookie usernameCookie = new Cookie("rememberUser", "");
                usernameCookie.setMaxAge(0);
                usernameCookie.setPath(request.getContextPath());
                response.addCookie(usernameCookie);
            }

            // ---------- 处理自动登录Cookie ----------
            if (autoLogin != null && !"0".equals(autoLogin)) {
                int autoLoginSeconds = Integer.parseInt(autoLogin);
                // Cookie值格式：用户名#密码（实际项目应加密，此处为教学演示）
                String cookieValue = URLEncoder.encode(username + "#" + password, "UTF-8");
                Cookie autoLoginCookie = new Cookie("autoLogin", cookieValue);
                autoLoginCookie.setMaxAge(autoLoginSeconds);
                autoLoginCookie.setPath(request.getContextPath());
                response.addCookie(autoLoginCookie);
            }

            // 重定向到首页
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            // 登录失败：提示错误并转发回登录页
            request.setAttribute("errorMsg", "用户名或密码错误，请重新登录！");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    /**
     * 验证用户名和密码是否正确
     * 教学演示使用硬编码，实际项目应查询数据库
     */
    private boolean validateUser(String username, String password) {
        return ("admin".equals(username) && "123456".equals(password))
                || ("user".equals(username) && "654321".equals(password));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
