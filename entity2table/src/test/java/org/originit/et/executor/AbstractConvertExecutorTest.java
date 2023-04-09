package org.originit.et.executor;


import org.junit.Test;
import org.originit.et.config.ConvertConfig;
import org.originit.et.executor.impl.SimpleConvertExecutor;

import java.util.ArrayList;

public class AbstractConvertExecutorTest {

    @Test
    public void testTypeMap() {
        final ArrayList<String> packages = new ArrayList<>();
        packages.add("org.originit.et.entity");
        final ConvertConfig convertConfig = ConvertConfig.builder().packageNames(packages)
                .url("jdbc:mysql://localhost:3306/sc_post?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false")
                .password("root")
                .username("root").build();
        final ConvertExecutor convertExecutor = new SimpleConvertExecutor(convertConfig);
        convertExecutor.execute();

    }
}