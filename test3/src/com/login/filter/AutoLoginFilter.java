package com.login.filter;

import com.login.bean.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 自动登录过滤器
 * 功能：在用户未登录时，检查是否存在自动登录Cookie
 *       如果Cookie有效，自动恢复用户的登录状态（免登录）
 */
@WebFilter("/*")
public class AutoLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        // 如果Session中已有用户，无需自动登录，直接放行
        User user = (User) session.getAttribute("user");
        if (user != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从Cookie中查找自动登录信息
        Cookie[] cookies = request.getCookies();
        String autoLoginValue = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("autoLogin".equals(cookie.getName())) {
                    autoLoginValue = cookie.getValue();
                    break;
                }
            }
        }

        // 如果存在自动登录Cookie，解析并验证用户信息
        if (autoLoginValue != null && !autoLoginValue.isEmpty()) {
            String decoded = URLDecoder.decode(autoLoginValue, "UTF-8");
            String[] parts = decoded.split("#");
            if (parts.length == 2) {
                String username = parts[0];
                String password = parts[1];

                // 验证用户名密码（与LoginServlet保持一致）
                if (validateUser(username, password)) {
                    User autoUser = new User(username, password);
                    session.setAttribute("user", autoUser);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 验证用户名和密码（与LoginServlet中的逻辑保持一致）
     */
    private boolean validateUser(String username, String password) {
        return ("admin".equals(username) && "123456".equals(password))
                || ("user".equals(username) && "654321".equals(password));
    }

    @Override
    public void destroy() {
    }
}
