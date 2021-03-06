package com.msn.poc.cart.docs;

import com.google.common.base.Predicates;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@ComponentScan("com.msn.poc.cart.controller")
public class SwaggerConfig {


    @Bean
    public Docket api() {
        /*
    	return new Docket(DocumentationType.SWAGGER_2)
                .select()
                        .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .apiInfo(apiInfo());
        */
        
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select() 
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                   .build();
    }
    

    private ApiInfo apiInfo() {
        String description = "CART microservice";
        return new ApiInfoBuilder()
                .title("Microservice")
                .description(description)
                //.termsOfServiceUrl("https://github.com/shamsnezami")
                //.license("Creative Commons (CC) with Accreditation to original author")
                //.licenseUrl("https://creativecommons.org/licenses/")
                .version("1.0")
                .build();
    }

}
