package com.zouzhao.common.security.config;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 姚超
 * @DATE: 2023-3-5
 */
@Configuration
public class FeignConfig {
    @Bean
    public Contract feignContract() {
        return new CustomContract();
    }

}
