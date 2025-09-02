package com.bitemate.config;

import com.bitemate.interceptor.JwtTokenAdminInterceptor;
import com.bitemate.interceptor.JwtTokenUserInterceptor;
import com.bitemate.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * Configuration class for registering web-layer components
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     *  Register custom interceptor
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("Starting to register custom interceptor...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/sendEmail")
                .excludePathPatterns("/user/user/login_email")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * Generate API documentation using Knife4j
     * @return
     */
    @Bean
    public Docket docketAdmin() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("BiteMate_Project_API_Documentation")
                .version("2.0")
                .description("BiteMate_Project_API_Documentation")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("Admin")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bitemate.controller.admin"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket docketUser() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("BiteMate_Project_API_Documentation")
                .version("2.0")
                .description("BiteMate_Project_API_Documentation")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("User")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bitemate.controller.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * Configure static resource mappings
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("file:" + basePath);
    }

    /**
     * Extend Message Converter
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters){
        log.info("Extend Message Converter start...");

        // Create a Message Converter object
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // Set a custom object mapper to handle JSON serialization/deserialization
        converter.setObjectMapper(new JacksonObjectMapper());

        // Add the custom message converter to the list of converters
        converters.add(0, converter);
    }
}
