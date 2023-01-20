package com.zouzhao.sys.org.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zouzhao.common.dto.ResultVO;
import com.zouzhao.common.service.BaseServiceImpl;
import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import com.zouzhao.sys.org.dao.SysOrgAccountDao;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import com.zouzhao.sys.org.entity.SysOrgAccount;
import com.zouzhao.sys.org.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.TimeUnit;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@Service
@RequestMapping("/api/sys-org/SysOrgAccount")
public class SysOrgAccountService extends BaseServiceImpl<SysOrgAccountDao, SysOrgAccount, SysOrgAccountVO> implements ISysOrgAccountApi {

    private static final Logger log = LoggerFactory.getLogger(SysOrgAccountService.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Value("${jwt.expirationTime}")
    private Integer expirationTime;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ResultVO<String> checkLogin(SysOrgAccountVO user) {
        log.debug("checkLogin->user:{}",user);
        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getFdLoginName(), user.getFdPassword());
            // 得到认证之后的信息
            Authentication authentication = authenticationManager.authenticate(authRequest);
            log.debug("login认证信息:{}",authentication);
            // 根据认证通过的authentication包装token，返回客户端
            String token = wrapAndStoreToken(authentication);
            log.debug("login,jwtToken:{}",token);
            return ResultVO.ok("登陆成功",token);
        } catch (AuthenticationException e) {
            log.error("登录发生异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultVO<String> layout(SysOrgAccountVO user) {
        //清空token
        SecurityContextHolder.getContext().setAuthentication(null);
        redisTemplate.delete("jwtToken:"+user.getFdLoginName());
        log.debug("用户{}退出成功",user.getFdLoginName());
        return ResultVO.ok("退出成功");
    }

    private String wrapAndStoreToken(Authentication authentication) {
        SysOrgAccount user = (SysOrgAccount) authentication.getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 切记不要暴露密码给客户端
            user.setFdPassword("******");
            String json = objectMapper.writeValueAsString(user);
            // 在容器中保留token
            // SecurityContextHolder.getContext().setAuthentication(authentication); // session.setAttribute(threadId, new UsernamePasswordToken());
            // servletContext.setAttribute(info.getId().toString(), json);
            String token = JwtUtils.generateToken(json);
            redisTemplate.opsForValue().set(token,user,expirationTime, TimeUnit.MINUTES);
            return token;
        } catch (JsonProcessingException e) {
            log.error("JSON解析出现异常", e);
            throw new RuntimeException(e);
        }
    }
}
