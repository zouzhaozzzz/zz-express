package com.zouzhao.common.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
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
        return Jwts.builder().signWith(getKey())
                .setSubject(data)
                .setExpiration( new Date(System.currentTimeMillis()+expirationTime*1000))
                .compact();
    }

    public static Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
    }

    private static SecretKey getKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    }

    public static void main(String[] args) throws InterruptedException {
        String username = "root";
        String key="zouzhao+zouzhao/next+affffffqwafaaaqhjkfahfjk";
        // 生成token
        // Date date = new Date(System.currentTimeMillis()+100*1000);
        //
        // String token = Jwts.builder().signWith(SignatureAlgorithm.HS256,key).setExpiration(date).setSubject(username).compact();
        // Thread.sleep(100);
        // Date date2 = new Date(System.currentTimeMillis()+100);
        //
        // String token2 = Jwts.builder().signWith(SignatureAlgorithm.HS256,key).setExpiration(date2).setSubject(username).compact();
        // System.out.println(token);
        // System.out.println(token2);

        String token3 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJvcmdBY2NvdW50SWRcIjpcIjFcIixcIm9yZ0FjY291bnRDcmVhdGVUaW1lXCI6XCIyMDIzLTAxLTI0IDE2OjQ1OjM1XCIsXCJvcmdBY2NvdW50QWx0ZXJUaW1lXCI6XCIyMDIzLTAyLTI2IDIwOjI0OjM4XCIsXCJvcmdBY2NvdW50TG9naW5OYW1lXCI6XCJyb290XCIsXCJvcmdBY2NvdW50UGFzc3dvcmRcIjpcIioqKioqKlwiLFwib3JnQWNjb3VudERlZlBlcnNvbklkXCI6XCIxNjI5NjkwMDc4ODQxMDczNjY2XCIsXCJvcmdBY2NvdW50RW5jcnlwdGlvblwiOlwiQkNyeXB0XCIsXCJvcmdBY2NvdW50U3RhdHVzXCI6bnVsbCxcImF1dGhvcml0aWVzXCI6bnVsbCxcImVuYWJsZWRcIjp0cnVlLFwicGFzc3dvcmRcIjpcIioqKioqKlwiLFwiYWNjb3VudE5vbkxvY2tlZFwiOnRydWUsXCJjcmVkZW50aWFsc05vbkV4cGlyZWRcIjp0cnVlLFwiYWNjb3VudE5vbkV4cGlyZWRcIjp0cnVlLFwidXNlcm5hbWVcIjpcInJvb3RcIn0iLCJleHAiOjE2NzgxMTEzMzB9.MOV3CfZup1VNFSyJCjtB0apoSWAOoGVr3YhpGkuQevkusernameroot";

        // 解析、校验token
        // String subject = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        // System.out.println(subject);
        // String subject2 = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token2).getBody().getSubject();
        // System.out.println(subject2);
        String subject3 = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token3).getBody().getSubject();
        System.out.println(subject3);
    }

}
