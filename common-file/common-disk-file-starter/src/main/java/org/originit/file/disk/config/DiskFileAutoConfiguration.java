package org.originit.file.disk.config;

import org.originit.file.disk.property.UploadConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({UploadConfig.class})
@Configuration
@ComponentScan(basePackages = "org.originit.file.disk")
public class DiskFileAutoConfiguration {

}
