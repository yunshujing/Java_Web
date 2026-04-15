package com.login.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 注销处理Servlet
 * 功能：清除Session中的用户信息 → 删除自动登录Cookie → 跳转到首页
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. 删除Session中保存的User对象
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("user");
            session.invalidate();
        }

        // 2. 删除自动登录的Cookie
        Cookie autoLoginCookie = new Cookie("autoLogin", "");
        autoLoginCookie.setMaxAge(0); // 立即过期
        autoLoginCookie.setPath(request.getContextPath());
        response.addCookie(autoLoginCookie);

        // 3. 跳转到index.jsp
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
