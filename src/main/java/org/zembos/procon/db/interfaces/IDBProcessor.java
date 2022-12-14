package org.zembos.procon.db.interfaces;

import org.zembos.procon.db.command.DBCommand;

/**
 * IDBProcessor used to process DB commands
 */
public interface IDBProcessor {
    /**
     * Process command
     * @param dbCommand which should be executed
     */
    void processDBCommand(DBCommand dbCommand);
}
