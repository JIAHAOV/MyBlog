package com.study.reproduce.confiig;

import com.study.reproduce.filter.SecurityLoginFilter;
import com.study.reproduce.handler.security.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LogoutSuccessHandler logoutSuccessHandler;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final CustomAccessDeniedHandler accessDeniedHandler;


    public SpringSecurityConfig(LogoutSuccessHandler logoutSuccessHandler, UserDetailsService userDetailsService,
                                PasswordEncoder passwordEncoder, CustomAccessDeniedHandler accessDeniedHandler) {
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .csrf().disable();
        http
                .authorizeRequests()//首先需要配置哪些请求会被拦截，哪些请求必须具有什么角色才能访问
                .antMatchers("/static/**", "/common/captcha", "/admin/login", "/admin/logout")
                .permitAll()//静态资源，使用permitAll来运行任何人访问（注意一定要放在前面）
                .antMatchers("/admin/**")
                .hasAnyRole("admin", "user", "visitor")
//                .hasAnyAuthority("admin", "user", "visitor")//admin下的所有请求必须登陆并且是admin角色才可以访问（不包含上面的静态资源）
                .and()
                .formLogin()//配置Form表单登陆
                .loginPage("/admin/login")//登陆页面地址（GET）
                .loginProcessingUrl("/admin/login")//form表单提交地址（POST）
                .permitAll()//登陆页面也需要允许所有人访问
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout", "GET"))
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .addFilter(new SecurityLoginFilter(authenticationManager()));//添加自定义过滤器

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }


}
