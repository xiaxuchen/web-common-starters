package com.originit.response.config;


import com.originit.response.property.ResponseProperty;
import com.originit.response.handler.ResponseResultHandler;
import com.originit.response.interceptor.ResponseResultInterceptor;
import com.originit.response.result.DefaultResultGenerator;
import com.originit.response.success.DefaultSuccessCodeAcquirer;
import com.originit.response.success.SuccessCodeAcquirer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 */
@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@ConditionalOnProperty(prefix = "common.response",name = "enable",havingValue = "true",matchIfMissing = true)
@EnableConfigurationProperties({ResponseProperty.class})
public class ResponseAutoConfiguration implements WebMvcConfigurer {


    // 添加自定义的MessageConverter
//    @Bean
//    public HttpMessageConverters customConverts () {
//        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
//    }
    private ResponseProperty responseProperty;

    public ResponseAutoConfiguration(ResponseProperty responseProperty) {
        this.responseProperty = responseProperty;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ResponseResultInterceptor(responseProperty)).addPathPatterns("/**");
    }

    @Bean
    public ResponseResultHandler responseResultHandler() {
        return new ResponseResultHandler();
    }

    @Bean
    public SuccessCodeAcquirer successCodeAcquirer() {
        return new DefaultSuccessCodeAcquirer();
    }

    @Bean
    public DefaultResultGenerator resultGenerator() {
        return new DefaultResultGenerator();
    }
}