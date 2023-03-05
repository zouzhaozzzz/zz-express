package com.zouzhao.sys.org.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 姚超
 * @DATE: 2023-3-3
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.zouzhao.sys.org.core.mapper")
@ComponentScan("com.zouzhao")
public class SysOrgApplication {
    public static void main(String[] args) {
        SpringApplication.run(SysOrgApplication.class,args);
    }
}
