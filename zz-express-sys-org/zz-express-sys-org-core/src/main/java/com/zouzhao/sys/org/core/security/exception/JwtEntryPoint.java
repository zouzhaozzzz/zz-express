package com.zouzhao.sys.org.core.security.exception;

import com.zouzhao.common.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class JwtEntryPoint implements AuthenticationEntryPoint {

    // 认证出现任何错误就会进入这个方法
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("认证JwtEntryPoint:commence()=>{}", authException.getMessage());
        //以JSON格式给前端响应
       // Response.err(, "认证失败");
        JsonUtils.writeToJson(response, HttpStatus.UNAUTHORIZED.value());
    }
}
