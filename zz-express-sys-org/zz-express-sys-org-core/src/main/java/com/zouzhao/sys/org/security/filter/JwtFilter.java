package com.zouzhao.sys.org.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zouzhao.sys.org.entity.SysOrgAccount;
import com.zouzhao.sys.org.entity.SysRightRole;
import com.zouzhao.sys.org.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtFilter extends OncePerRequestFilter { // 保证每一个请求指挥触发一次doFilter逻辑

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.expirationTime}")
    private Integer expirationTime;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, RuntimeException {
        // 首先从request中获取token信息
        String jwtToken = request.getHeader("jwtToken");
        log.debug("接收到的jwtToken:{}", jwtToken);
        //如果是登陆请求不走filter
        if ((request.getRequestURI().matches("[a-z/]+[-][a-z/,A-Z/]+checkLogin$") && request.getMethod().equals("POST"))
                || StringUtils.isEmpty(jwtToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        // jwtToken证明是非登录请求，需要对jwtToken进行有效性检查

        Jws<Claims> claims = JwtUtils.getClaims(jwtToken);
        String subject = claims.getBody().getSubject();
        ObjectMapper mapper = new ObjectMapper();
        SysOrgAccount user = mapper.readValue(subject, SysOrgAccount.class);
        String redisKey = "jwtToken:" + user.getOrgAccountLoginName();
        List<SysRightRole> roles =  (List<SysRightRole>)redisTemplate.opsForValue().get(redisKey);
        if (ObjectUtils.isEmpty(roles))
            throw new RuntimeException("该用户已经注销了");
        redisTemplate.opsForValue().set(redisKey, roles, expirationTime, TimeUnit.SECONDS);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getOrgAccountLoginName(),
                null,
                // AuthorityUtils.commaSeparatedStringToAuthorityList("default")
                roles);
        // authentication.setAuthenticated(true);
        //把认证信息放入到SecurityContextHolder中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 把请求往后传递（DispatcherServlet -> Controller）
        filterChain.doFilter(request, response);

    }

}
