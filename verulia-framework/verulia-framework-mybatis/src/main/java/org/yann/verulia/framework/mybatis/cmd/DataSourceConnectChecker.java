package org.yann.verulia.framework.mybatis.cmd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.yann.verulia.framework.mybatis.exception.DataSourceConnectException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接启动自检
 * <p>
 * 作用：在服务启动时主动连接一次数据库。
 * 若连接失败，打印醒目日志并强制终止服务，防止带着病态启动。
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class DataSourceConnectChecker implements CommandLineRunner {

    private final DataSource dataSource;
    private final ConfigurableApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        log.info(" --->>> 正在检查数据库连接 <<<---");
        try (Connection connection = dataSource.getConnection()) {
            log.info(" -> 数据库连接成功！URL: {}", connection.getMetaData().getURL());
        } catch (SQLException e) {
            log.error("========================================================");
            log.error("                   数据库连接失败！                       ");
            log.error("    异常信息: {}", e.getMessage());
            log.error("========================================================");
            applicationContext.close();
            throw new DataSourceConnectException();
        }
    }
}