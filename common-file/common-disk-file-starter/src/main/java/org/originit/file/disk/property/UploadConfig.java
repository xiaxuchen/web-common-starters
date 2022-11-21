package org.originit.file.disk.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "common.file.disk")
@Data
public class UploadConfig {

    private String rootPath;

}
