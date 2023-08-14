package org.originit.et.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xxc
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConvertConfig {

    /**
     * 实体类所在的包的包名
     */
    private List<String> packageNames;

    /**
     * 表对应实体类的类名
     */
    private List<String> tableClassNames;

    /**
     * 表信息获取器
     */
    private String tableInfoAcquirerClassName;


    private String columnInfoAcquirerClassName;

    /**
     * 数据库相关配置
     */
    private String url;

    private String username;

    private String password;
}
