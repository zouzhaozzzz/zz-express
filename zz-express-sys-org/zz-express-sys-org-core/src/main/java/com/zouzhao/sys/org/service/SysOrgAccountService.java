package com.zouzhao.sys.org.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.common.dto.Response;
import com.zouzhao.common.service.BaseServiceImpl;
import com.zouzhao.sys.org.api.ISysOrgAccountApi;
import com.zouzhao.sys.org.dto.SysOrgAccountVO;
import com.zouzhao.sys.org.entity.SysOrgAccount;
import com.zouzhao.sys.org.mapper.SysOrgAccountMapper;
import com.zouzhao.sys.org.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@Service
@RestController
@RequestMapping("/api/sys-org/sysOrgAccount")
public class SysOrgAccountService extends BaseServiceImpl<SysOrgAccountMapper, SysOrgAccount, SysOrgAccountVO> implements ISysOrgAccountApi {

    private static final Logger log = LoggerFactory.getLogger(SysOrgAccountService.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Value("${jwt.expirationTime}")
    private Integer expirationTime;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Response<String>
    checkLogin(SysOrgAccountVO user) {
        log.debug("checkLogin->user:{}", user);
        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getOrgAccountLoginName(), user.getOrgAccountPassword());
            // 得到认证之后的信息
            Authentication authentication = authenticationManager.authenticate(authRequest);
            log.debug("login认证信息:{}", authentication);
            // 根据认证通过的authentication包装token，返回客户端
            String token = wrapAndStoreToken(authentication);
            return Response.ok("登陆成功", token);
        } catch (AuthenticationException e) {
            log.error("登录发生异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response<String> layout(SysOrgAccountVO user) {
        //清空token和authentication.context
        redisTemplate.delete("jwtToken:" + user.getOrgAccountLoginName());
        SecurityContextHolder.getContext().setAuthentication(null);
        log.debug("用户{}退出成功", user.getOrgAccountLoginName());
        return Response.ok("退出成功");
    }

    @Override
    public IdDTO add(SysOrgAccountVO vo) {
        String password = vo.getOrgAccountPassword();
        if (ObjectUtils.isEmpty(password)) return IdDTO.of("-1");
        vo.setOrgAccountPassword(passwordEncoder.encode(password));
        return super.add(vo);
    }

    private String wrapAndStoreToken(Authentication authentication) {
        SysOrgAccount user = (SysOrgAccount) authentication.getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 切记不要暴露密码给客户端
            user.setOrgAccountPassword("******");
            String json = objectMapper.writeValueAsString(user);
            // 在容器中保留token
            // SecurityContextHolder.getContext().setAuthentication(authentication); // session.setAttribute(threadId, new UsernamePasswordToken());
            // servletContext.setAttribute(info.getId().toString(), json);
            String token = JwtUtils.generateToken(json);
            redisTemplate.opsForValue().set("jwtToken:" + user.getOrgAccountLoginName(), user, expirationTime, TimeUnit.SECONDS);
            return token;
        } catch (JsonProcessingException e) {
            log.error("JSON解析出现异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public SysOrgAccount voToEntity(SysOrgAccountVO vo) {
        SysOrgAccount sysOrgAccount = new SysOrgAccount();
        BeanUtils.copyProperties(vo, sysOrgAccount);
        return sysOrgAccount;
    }

    public SysOrgAccount findById(IdDTO of) {
        return getMapper().findById(of.getId());
    }
}



