package org.zembos.procon.db.command;

import org.zembos.procon.model.User;

/**
 * DBCommand type which represent command which can be processed
 * @param type of DB operation
 * @param user entity to be stored (candidate for refactoring:))
 */
public record DBCommand(DBCommand.Type type, User user) implements IDBCommand {
    public enum Type {
        SELECT,
        INSERT,
        DELETE
    }
}
