package com.zouzhao.sys.org.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static String secretString;
    private static Integer expirationTime;

    @Value("${jwt.secretString}")
    public  void setSecretString(String secretString) {
        JwtUtils.secretString = secretString;
    }

    @Value("${jwt.expirationTime}")
    public  void setExpirationTime(Integer expirationTime) {
        JwtUtils.expirationTime = expirationTime;
    }

    public static String generateToken(String data) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS256,secretString)
                .setSubject(data).
                setExpiration( new Date(System.currentTimeMillis()+expirationTime*1000))
                .compact();
    }

    public static Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretString).build().parseClaimsJws(token);
    }


    // public static void main(String[] args) throws InterruptedException {
    //     String username = "root";
    //
    //     // 生成token
    //     Date date = new Date(System.currentTimeMillis()+100*1000);
    //
    //     String token = Jwts.builder().signWith(SignatureAlgorithm.HS256,secretString).setExpiration(date).setSubject(username).compact();
    //     Thread.sleep(100);
    //     Date date2 = new Date(System.currentTimeMillis()+100);
    //
    //     String token2 = Jwts.builder().signWith(SignatureAlgorithm.HS256,secretString).setExpiration(date2).setSubject(username).compact();
    //     System.out.println(token);
    //     System.out.println(token2);
    //
    //     String token3 = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzQwMjIzNDgsInN1YiI6InJvb3QifQ.Iwy8HPEg62U9w1QtCo9WfQdq8Q6uex1NYpIR6fJKRss";
    //
    //     // 解析、校验token
    //     String subject = Jwts.parserBuilder().setSigningKey(secretString).build().parseClaimsJws(token).getBody().getSubject();
    //     System.out.println(subject);
    //     // String subject2 = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token2).getBody().getSubject();
    //     // System.out.println(subject2);
    //     String subject3 = Jwts.parserBuilder().setSigningKey(secretString).build().parseClaimsJws(token3).getBody().getSubject();
    //     System.out.println(subject3);
    // }

}
