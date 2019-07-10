package com.yangle.demo.filter;

import com.google.common.collect.Lists;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "actionFilter", urlPatterns = "/uco")
public class ActionFilter implements Filter {

    private List<String> actions = Lists.newArrayList("CreateDBInstance", "DeleteDBInstance");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String action = servletRequest.getParameter("Action");

        if (actions.contains(action)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else { // 重定向到错误提示
            servletRequest.getRequestDispatcher("/failed").forward(servletRequest, servletResponse);
        }

    }
}
