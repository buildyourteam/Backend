package com.eskiiimo.api.error.filters;

import com.eskiiimo.api.error.util.MultiReadableHttpServletRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MultiReadableHttpServletRequestFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        MultiReadableHttpServletRequest multiReadRequest = new MultiReadableHttpServletRequest((HttpServletRequest) req);
        chain.doFilter(multiReadRequest, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}
