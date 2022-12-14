package org.zembos.procon.service.interfaces;

/**
 * Command consumer service interface
 */
public interface ICommandConsumerService {
    /**
     * This method executes until handles all commands from queue
     */
    void consumeCommands();
}
