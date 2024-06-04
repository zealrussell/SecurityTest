package com.zeal.securitytest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeal.securitytest.filter.LoginKaptchaFilter;
import com.zeal.securitytest.service.MyUserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.filters.CorsFilter;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.util.ObjectUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/26 12:36
 * @Version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class WebSecurityConfigurer {

    @Value("${spring.datasource.driver-class-name}")
    private final String driverClassName;
    @Value("${spring.datasource.url}")
    private final String url;
    @Value("${spring.datasource.username}")
    private final String username;
    @Value("${spring.datasource.password}")
    private final String password;

    private final MyUserDetailService myUserDetailService;

    // private final FindByIndexNameSessionRepository findByIndexNameSessionRepository;
    @Autowired
    public WebSecurityConfigurer(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }
    @Bean
    public DataSource dataSource() {
        return new PooledDataSource(driverClassName, url, username, password);
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        return sqlSessionFactoryBean;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        // 放行资源
                        .requestMatchers("/login.html").permitAll()
                        .requestMatchers("/index").permitAll()
                        .requestMatchers("/hello").permitAll()
                        .requestMatchers("/vc.jpg").permitAll()
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login.html")   // 指定默认登录页面
                        .loginProcessingUrl("/doLogin")  // 指定处理登录请求的URL
                        .usernameParameter("uname")
                        .passwordParameter("passwd")
                        .successHandler(new MyAuthenticationSuccessHandler())
                        //.failureForwardUrl("/login.html")  // 认证失败后 forward
                        // .failureUrl("/")  // 认证失败后 redirect
                        .failureHandler(new MyAuthenticationFailureHandler())

                )
                .logout(out -> out
                        .logoutUrl("/logout")   // 注销操作的url，默认 GET POST
                        .logoutSuccessUrl("/login.html")
                        .invalidateHttpSession(true) // 是否清除session
                        .clearAuthentication(true)  // 清除认证标记
                )
                // 处理
                .exceptionHandling(eh-> eh
                        .authenticationEntryPoint((req, resp, ex) -> {
                            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                            resp.getWriter().println(ex.getMessage());
                            resp.getWriter().println("验证后才能访问");
                        })
                )
                // 记住我
                .rememberMe(rm -> rm
                        .alwaysRemember(false)  // 是否总是记住我
                        .rememberMeCookieDomain("remember-me")  // 表单字段
                        .rememberMeCookieName("remember-me")  // cookie名称
                        .tokenRepository(persistentTokenRepository())  //
                )
                // session管理
                .sessionManagement(sm -> sm
                        // 最大session并发数
                        .maximumSessions(3)
                        // 禁止再次登录,当session满了后不允许后来者登录
                        .maxSessionsPreventsLogin(true)
                        // 用户被挤下线后的跳转页面
                        .expiredUrl("/login.html")
                        // 下线后处理操作,在前后端分离时返回json
                        .expiredSessionStrategy(event -> {
                            HttpServletResponse response = event.getResponse();
                            Map<String, Object> result = new HashMap<>();
                            result.put("status", 500);
                            result.put("msg", "当前会话已经失效,请重新登录");

                            String s = new ObjectMapper().writeValueAsString(result);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().println(s);
                            response.flushBuffer();
                        })
                        // session管理
                        // .sessionRegistry(springSessionBackedSessionRegistry())
                )
                .csrf(AbstractHttpConfigurer::disable)  // 关闭csrf
                // 跨域
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .httpBasic(withDefaults());
        // 替换filter
        http.addFilterAt(loginKaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 指定数据库持久化
    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(null);
        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    // session
//    @Bean
//    public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry() {
//        return new SpringSessionBackedSessionRegistry(findByIndexNameSessionRepository);
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailService);
        ProviderManager pm = new ProviderManager(daoAuthenticationProvider);
        return pm;
    }

    // 密码
    @Bean
    public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginKaptchaFilter loginKaptchaFilter() throws Exception {
        LoginKaptchaFilter loginKaptchaFilter = new LoginKaptchaFilter();
        loginKaptchaFilter.setFilterProcessesUrl("/doLogin");
        loginKaptchaFilter.setUsernameParameter("uname");
        loginKaptchaFilter.setPasswordParameter("passwd");
        loginKaptchaFilter.setKaptchaParameter("kaptcha");
        loginKaptchaFilter.setAuthenticationManager(authenticationManagerBean());
        return loginKaptchaFilter;
    }

    // 方式一:spring的跨域
//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilter() {
//        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
//        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
//        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
//        corsConfiguration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        registrationBean.setFilter(new CorsFilter());
//        registrationBean.setOrder(-1);
//        return registrationBean;
//    }
    // 方式二:
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
