/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.sixj.multilingual.filter;

import com.sixj.multilingual.utils.LanguageInfoUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author sixiaojie
 * @date 2019-12-13 18:07
 */
@Component
public class LanguageInfoFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            LanguageInfoUtil.removeLanguageInfo();
        }
    }

    @Override
    public void destroy() {

    }
}
