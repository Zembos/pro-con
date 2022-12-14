package org.zembos.procon.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class HikariCPDataSource {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        log.debug("Configuring DB...");
        config.setJdbcUrl("jdbc:h2:mem:;DB_CLOSE_DELAY=-1;INIT=runscript from 'classpath:/db.sql'");
        config.setUsername("sa");
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private HikariCPDataSource(){
    }
}
