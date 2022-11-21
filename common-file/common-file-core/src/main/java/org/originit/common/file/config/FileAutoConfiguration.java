package org.originit.common.file.config;

import org.originit.common.file.FileFilter;
import org.originit.common.file.FileUploadStrategy;
import org.originit.common.file.FileUploaderDeprecated;
import org.originit.common.file.impl.DefaultFileFilter;
import org.originit.common.file.impl.SimpleFileUploader;
import org.originit.common.file.property.FileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xxc
 */
//@Configuration
@EnableConfigurationProperties({FileProperties.class})
@ComponentScan("org.originit.common.file")
public class FileAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FileFilter.class)
    public FileFilter defaultFileFilter(FileProperties fileProperties) {
        return new DefaultFileFilter(fileProperties);
    }

    @Bean
    @ConditionalOnMissingBean(FileUploaderDeprecated.class)
    public FileUploaderDeprecated defaultFileUploader(FileProperties fileProperties, FileUploadStrategy strategy, FileFilter filter) {
        final SimpleFileUploader simpleFileUploader = new SimpleFileUploader(fileProperties);
        simpleFileUploader.setFilter(filter);
        simpleFileUploader.setStrategy(strategy);
        return simpleFileUploader;
    }


}
