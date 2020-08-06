package com.eskiiimo.web.security.config;

import com.eskiiimo.web.security.filter.JwtAuthenticationFilter;
import com.eskiiimo.web.security.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정
 *
 * @author always0ne
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    /**
     * Spring Security 설정
     * URL, 메소드별 접근권한 설정
     * JWT 인증 필터 추가
     *
     * @param http HttpSecurity
     * @see "JwtAuthenticationFilter"
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .cors().and()
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은 필요없으므로 생성안함.
                .and()
                .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
                .antMatchers("/auth/*").permitAll() // 가입 및 인증 주소는 누구나 접근가능
                .antMatchers(
                        HttpMethod.GET,
                        "/projects/*/apply",
                        "/projects/*/apply/*",
                        "/projects/*/recruits",
                        "/profile/*/recruit",
                        "/profile/*/recruit/*",
                        "/profile/*/*/hidden"
                ).hasRole("USER")
                .antMatchers(HttpMethod.GET, "/**").permitAll() // 나머지 GET요청 리소스는 누구나 접근가능
                .anyRequest().hasRole("USER") // Get을 제외한 모든 요청은 인증된 회원만 접근 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class); // jwt token 필터를 id/password 인증 필터 전에 넣는다

    }

    /**
     * PasswordEncoder Bean
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
