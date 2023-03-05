package com.zouzhao.opt.manage.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.zouzhao.opt.manage.core.mapper")
@EnableFeignClients({"com.zouzhao"})
@ComponentScan("com.zouzhao")
public class OptManageApplication {
    public static void main(String[] args) {
    SpringApplication.run(OptManageApplication.class,args);
    }
}
