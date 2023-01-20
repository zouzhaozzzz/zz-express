package com.zouzhao.sys.org.security.filter;

import com.zouzhao.sys.org.entity.SysOrgAccount;
import com.zouzhao.sys.org.service.SysOrgAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter { // 保证每一个请求指挥触发一次doFilter逻辑

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SysOrgAccountService sysOrgAccountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 首先从request中获取token信息
        String jwtToken = request.getHeader("jwtToken");
        log.debug("接收到的wtToken:{}",jwtToken);
        //如果是登陆请求不走filter
        if ( (request.getRequestURI().matches("[a-z/]+[-][a-z/,A-Z/]+checkLogin$") && request.getMethod().equals("POST"))
                || StringUtils.isEmpty(jwtToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        // jwtToken证明是非登录请求，需要对jwtToken进行有效性检查
        try {
            SysOrgAccount userInfo = (SysOrgAccount) redisTemplate.opsForValue().get(jwtToken);
            if(ObjectUtils.isEmpty(userInfo))throw new RuntimeException("该用户已经注销了");

            SysOrgAccount sysOrgAccount = sysOrgAccountService.getById(userInfo.getFdId());

            log.debug("doFilterInternal:{},认证信息：{}", "认证通过...", sysOrgAccount);

            //TODO 去dao查询用户权限
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    sysOrgAccount.getFdLoginName(),
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("default"));
            // authentication.setAuthenticated(true);
            //把认证信息放入到SecurityContextHolder中
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 把请求往后传递（DispatcherServlet -> Controller）
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Token无效", e);
            throw new RuntimeException(e);
        }
    }

}
