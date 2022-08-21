package org.originit.mp.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.List;

/**
 * mybatis plus代码自动生成
 */
public class CodeGenerator {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config {
        private boolean isOverwrite = false;
        private String outputDir;
        private String author;
        private String packageName;
        private String moduleName;
        private String mapperPath;
        private List<String> tableNames;
        private List<String> tablePrefixes;
        private String url;
        private String username;
        private String password;

        private boolean enableSwagger = false;

        /**
         * 从spring上下文获取配置
         * @param env 环境
         */
        public void loadDataSourceConfigFromFile(Environment env) {
            Config config = this;
            config.setUrl(env.getProperty("spring.datasource.url"));
            config.setUsername(env.getProperty("spring.datasource.username"));
            config.setPassword(env.getProperty("spring.datasource.password"));
        }
    }



    public void generate(Config config) {
        FastAutoGenerator.create(config.url, config.username, config.password)
                .globalConfig(builder -> {
                    builder.author(config.author) // 设置作者
                            .outputDir(config.getOutputDir()); // 指定输出目录
                    if (config.isOverwrite) {
                        builder.fileOverride();
                    }
                    if (config.enableSwagger) {
                        builder.enableSwagger();
                    }
                })
                .packageConfig(builder -> {
                    builder.parent(config.packageName) // 设置父包名
                            .moduleName(config.moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, config.mapperPath)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(config.tableNames) // 设置需要生成的表名
                            .addTablePrefix(config.tablePrefixes)
                            .entityBuilder().columnNaming(NamingStrategy.underline_to_camel)
                            .enableLombok(); // 设置过滤表前缀
                })

                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();

    }
}
