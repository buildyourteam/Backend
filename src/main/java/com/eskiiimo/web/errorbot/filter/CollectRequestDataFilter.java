package com.eskiiimo.web.errorbot.filter;

import com.eskiiimo.web.errorbot.util.RequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.eskiiimo.web.errorbot.util.AgentUtils.getAgentDetail;
import static com.eskiiimo.web.errorbot.util.MDCUtil.*;

/**
 * Request 정보들을 수집하여 MDC에 보관하는 필터
 *
 * @author always0ne
 * @version 1.0
 */
public class CollectRequestDataFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWrapper requestWrapper = RequestWrapper.of(request);

        setJsonValueAndPutMDC(HEADER_MAP_MDC, requestWrapper.headerMap());
        setJsonValueAndPutMDC(PARAMETER_MAP_MDC, requestWrapper.parameterMap());
        setJsonValueAndPutMDC(BODY_MDC, requestWrapper.body());
        setJsonValueAndPutMDC(AGENT_DETAIL_MDC, getAgentDetail((HttpServletRequest) request));
        putMDC(REQUEST_URI_MDC, requestWrapper.getRequestUri());

        try {
            chain.doFilter(request, response);
        } finally {
            clear();
        }
    }
}