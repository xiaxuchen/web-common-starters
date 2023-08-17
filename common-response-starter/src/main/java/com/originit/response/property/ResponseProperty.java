package com.originit.response.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xxc
 */
@ConfigurationProperties(prefix = "common.response")
@Data
public class ResponseProperty {

    // 是否启用
    private Boolean enable = true;

    private Integer successCode = 1;

    private Map<String, String> fieldNames = new HashMap<>();

}
