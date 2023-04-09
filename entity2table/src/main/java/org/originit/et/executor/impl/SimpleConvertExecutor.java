package org.originit.et.executor.impl;

import cn.hutool.db.ds.simple.SimpleDataSource;
import lombok.extern.slf4j.Slf4j;
import org.originit.et.config.ConvertConfig;
import org.originit.et.entity.Table;
import org.originit.et.executor.AbstractConvertExecutor;
import org.originit.et.util.TemplateUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author xxc
 */
@Slf4j
public class SimpleConvertExecutor extends AbstractConvertExecutor {

    public SimpleConvertExecutor(ConvertConfig config) {
        super(config);
    }


    private static DataSource ds;
    private synchronized static DataSource getDs(ConvertConfig config) {
        if (ds != null) {
            return ds;
        }
        //具体的配置参数请参阅Druid官方文档
        ds = new SimpleDataSource(config.getUrl(), config.getUsername(), config.getPassword());
        return ds;
    }

    @Override
    public void execute() {
        // 1. 获取到要转换的表
        List<Table> tableList = this.tables;
        // 2. 获取jdbc执行
        try {
            final Connection connection = getDs(this.config).getConnection();
            for (Table table : tableList) {
                final String template = TemplateUtil.getTemplate("/table/create.ftl", table);
                log.debug(template);
                connection.createStatement().execute(template);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
