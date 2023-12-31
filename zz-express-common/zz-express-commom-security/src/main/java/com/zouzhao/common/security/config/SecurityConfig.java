package com.zouzhao.common.security.config;

import com.zouzhao.common.security.filter.JwtFilter;
import com.zouzhao.common.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 开启基于方法的权限判断
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    AccessDeniedHandler accessDeniedHandler;
    @Resource
    private MyUserDetailsService myUserDetailsService;

    @Bean // 产生一个bean
    public PasswordEncoder passwordEncoder() {
        // 使用不可逆的加密算法实现的（相同的输入，不一定会有相同的输出）
        // 破解这种算法，只能穷举
        return new BCryptPasswordEncoder();
    }

    @Bean // 产生authenticationManager的Bean，发布到spring的容器中，方便自己的类能够注入
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);
        http
                //         // 配置自定义的登陆页面
                //         .formLogin()
                //         .loginPage("/loginx.html") // static 、 template
                //         .loginProcessingUrl("/loginx") // 表单提交路径,最终这个表单的数据，是交给了security的过滤器链来进行处理的
                //         // .successForwardUrl()
                //         .defaultSuccessUrl("/success.html") // 配置登录以后访问的路径
                //         .permitAll() // 允许访问
                // // 设置其它的路径的权限信息
                //         .and().authorizeRequests() // 其它需要授权的请求
                //         .antMatchers("/movie","/a","/b").permitAll()
                //
                //         // 用来判断当前访问的路径是否具有“某一个权限”
                //         // .antMatchers("/category").hasAuthority("update")
                //         // 只要具有abc中的任意一个就能访问
                //         // .antMatchers("/category").hasAnyAuthority("a","b","c")
                //
                //         .antMatchers("/category").hasRole("guest")
                //         // .antMatchers("/category").hasAnyRole("admin", "guest")
                //         .anyRequest().authenticated() // 除了上面限定的认证才能访问的请求以外，其它的请求都需认证以后才能访问
                //         // 关闭security自带的csrf
                //         .and()
                //         .csrf().disable();

                // 由前端项目提供的
                .authorizeRequests(
                        authorize -> authorize
                                // 允许访问登录
                                .antMatchers("/data/sys-org/sysOrgAccount/checkLogin").permitAll()
                                .antMatchers("/data/sys-org/sysOrgAccount/layout").permitAll()
                                .antMatchers("/data/**").authenticated()
                                .antMatchers("/api/**").permitAll()
                                // .anyRequest().authenticated()
                )
                // 关闭跨域攻击后台响应的防护逻辑
                .csrf().disable()
                // 把JwtFilter注册在xxx的前面
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加认证和鉴权的错误处理
                .exceptionHandling(
                        e ->
                                // 认证的异常处理
                                e.authenticationEntryPoint(authenticationEntryPoint)
                                        // 鉴权错误
                                        .accessDeniedHandler(accessDeniedHandler)
                )
                // 配置security的cors
                .cors();
    }

    public static void main(String[] args) {
        String password = new BCryptPasswordEncoder().encode("root");
        System.out.println(password);
        System.out.println(new BCryptPasswordEncoder().matches("root", "$2a$10$Xr7JX9.v03loxD.D5UzUoOmHsW6YaW4.DRepeFV61iiMDY4/SpaE6"));
    }


}
