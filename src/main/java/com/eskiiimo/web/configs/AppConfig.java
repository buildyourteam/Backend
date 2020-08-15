package com.eskiiimo.web.configs;


import com.eskiiimo.web.errorbot.filters.LogbackMdcFilter;
import com.eskiiimo.web.errorbot.filters.MultiReadableHttpServletRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;


@Configuration
public class AppConfig {
    @Bean
    FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ForwardedHeaderFilter());
        return bean;
    }

    //logack
    @Bean
    public FilterRegistrationBean multiReadableHttpServletRequestFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        MultiReadableHttpServletRequestFilter multiReadableHttpServletRequestFilter = new MultiReadableHttpServletRequestFilter();
        registrationBean.setFilter(multiReadableHttpServletRequestFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean logbackMdcFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LogbackMdcFilter logbackMdcFilter = new LogbackMdcFilter();
        registrationBean.setFilter(logbackMdcFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
