package org.zembos.procon.db;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zembos.procon.db.command.DBCommand;
import org.zembos.procon.db.interfaces.IDBProcessor;
import org.zembos.procon.model.User;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Database configuration
 */
@Getter
@Setter
@Slf4j
public class DBProcessor implements IDBProcessor {

    public DBProcessor() {
    }

    @Override
    public void processDBCommand(DBCommand dbCommand) {
        switch (dbCommand.type()) {
            case INSERT -> {
                log.info("Processing INSERT command {}", dbCommand);
                if (dbCommand.user() != null) {
                    insertRecord(dbCommand.user());
                } else {
                    log.error("Missing user when trying to insert into DB");
                }
            }
            case DELETE -> {
                log.info("Processing DELETE command {}", dbCommand);
                delete();
            }
            case SELECT -> {
                log.info("Processing SELECT command {}", dbCommand);
                printAll();
            }
            default -> log.warn("Unknown DB command");
        }
    }

    private void insertRecord(User user) {
        var insertSql = "insert into SUSERS VALUES(?, ?, ?)";
        try (Connection connection = HikariCPDataSource.getConnection()) {
            var statement = connection.prepareStatement(insertSql);
            statement.setInt(1, user.id());
            statement.setString(2, user.guid());
            statement.setString(3, user.name());
            statement.execute();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private void delete() {
        var deleteSql = "DELETE FROM SUSERS";
        try (Connection connection = HikariCPDataSource.getConnection()) {
            connection.createStatement().execute(deleteSql);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private void printAll() {
        var selectSql = "select * from SUSERS";
        try (Connection connection = HikariCPDataSource.getConnection()) {
            var result = connection.createStatement().executeQuery(selectSql);
            while (result.next()) {
                var user = new User(result.getInt(1), result.getString(2), result.getString(3));
                System.out.println(user);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
