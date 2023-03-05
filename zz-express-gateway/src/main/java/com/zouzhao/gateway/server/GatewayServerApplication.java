package com.zouzhao.gateway.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 姚超
 * @DATE: 2023-1-24
 */
@SpringBootApplication
@EnableDiscoveryClient
// @EnableFeignClients("com.zouzhao")
// @ComponentScan("com.zouzhao")
public class GatewayServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class,args);
    }
}
