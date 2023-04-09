package com.originit.common.config;

import com.originit.common.param.anno.BodyFieldArgResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CommonAutoConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnProperty(name = "common.mvc.bodyField", matchIfMissing = true, havingValue = "true")
    public BodyFieldArgResolver bodyFieldArgResolver() {
        return new BodyFieldArgResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        final BodyFieldArgResolver bodyFieldArgResolver = bodyFieldArgResolver();
        if (bodyFieldArgResolver != null) {
            resolvers.add(bodyFieldArgResolver);
        }
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
