package com.login.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 中文乱码过滤器
 * 功能：对所有请求和响应统一设置UTF-8编码，解决中文乱码问题
 * 拦截所有请求（/*），确保在Servlet/JSP处理之前完成编码设置
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 支持在web.xml中配置编码参数，未配置则使用默认UTF-8
        String configEncoding = filterConfig.getInitParameter("encoding");
        if (configEncoding != null && !configEncoding.trim().isEmpty()) {
            this.encoding = configEncoding;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 设置请求编码（解决POST提交的中文乱码）
        request.setCharacterEncoding(encoding);

        // 设置响应编码和内容类型
        response.setCharacterEncoding(encoding);
        response.setContentType("text/html;charset=" + encoding);

        // 继续执行过滤链
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 过滤器销毁时的清理工作
    }
}
