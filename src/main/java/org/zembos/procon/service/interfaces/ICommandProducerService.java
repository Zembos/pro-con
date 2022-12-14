package org.zembos.procon.service.interfaces;

import org.zembos.procon.db.command.IDBCommand;

/**
 * Command producer service interface
 */
public interface ICommandProducerService {
    /**
     * Produce DB command
     * @param command which is going to be produced
     */
    void produceCommand(IDBCommand command);
}
