package com.zouzhao.common.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${module.moduleNameList}")
    private List<String> moduleNameList;

    public SwaggerConfig() {
    }

    @Autowired
    public void dynamicConfiguration(ApplicationContext applicationContext) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        ApiInfoBuilder apiInfoBuilder = (new ApiInfoBuilder()).description("zz-express前端后端对应接口").version("1.0.0").license("个人使用.");
        moduleNameList.forEach(moduleName -> {
            Docket docket = (new Docket(DocumentationType.SWAGGER_2)).
                    groupName(moduleName).apiInfo(apiInfoBuilder.title(moduleName).build())
                    .select().apis(this.genSubPackage(moduleName))
                    .paths(Predicates.or(PathSelectors.ant("/data/**"), PathSelectors.ant("/api/**"))).build();
            beanFactory.registerSingleton("swagger-"+moduleName, docket);
        });
    }


    private Predicate<RequestHandler> genSubPackage(String moduleName) {
        return RequestHandlerSelectors.basePackage("com.zouzhao." + moduleName.replace("-", "."));
    }
}
