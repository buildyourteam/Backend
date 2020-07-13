package com.eskiiimo.web.error.filters;

import com.eskiiimo.web.error.util.HttpUtils;
import com.eskiiimo.web.error.util.MDCUtil;
import com.eskiiimo.web.error.util.RequestWrapper;
import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;

public class LogbackMdcFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWrapper requestWrapper = RequestWrapper.of(request);

        // Set Http Header
        MDCUtil.setJsonValue(MDCUtil.HEADER_MAP_MDC, requestWrapper.headerMap());

        // Set Http params
        MDCUtil.setJsonValue(MDCUtil.PARAMETER_MAP_MDC, requestWrapper.parameterMap());

        // Set Http Body
        MDCUtil.setJsonValue(MDCUtil.BODY_MDC, requestWrapper.body());

        // If you use SpringSecurity, you could SpringSecurity UserDetail Information.
        MDCUtil.setJsonValue(MDCUtil.USER_INFO_MDC, HttpUtils.getCurrentUser());

        // Set Agent Detail
       // MDCUtil.setJsonValue(MDCUtil.AGENT_DETAIL_MDC, AgentUtils.getAgentDetail((HttpServletRequest) request));

        // Set Http Request URI
        MDCUtil.set(MDCUtil.REQUEST_URI_MDC, requestWrapper.getRequestUri());

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {

    }
}