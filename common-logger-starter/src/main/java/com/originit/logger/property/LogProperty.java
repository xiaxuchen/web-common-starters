package com.originit.logger.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("common.logger")
@Data
public class LogProperty {

    private boolean logGet = true;

    private boolean enable = true;

    private boolean debug = false;
}
