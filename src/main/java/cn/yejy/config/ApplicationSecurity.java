package cn.yejy.config;

import cn.yejy.constant.RoleConstant;
import cn.yejy.filter.AuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/authorize/**").permitAll()
                .antMatchers("/user/**").hasAnyAuthority(RoleConstant.USER, RoleConstant.SYS_ADMIN)
                .and()
                .csrf()
                .disable()
                .sessionManagement() // 定制我们自己的 session 策略
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 调整为让 Spring Security 不创建和使用 session
                .and().addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }
}
