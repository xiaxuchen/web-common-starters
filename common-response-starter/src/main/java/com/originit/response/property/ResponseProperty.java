package com.originit.response.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xxc
 */
@ConfigurationProperties(prefix = "common.response")
@Data
public class ResponseProperty {

    // 是否启用
    private Boolean enable = true;

}
