package org.zembos.procon.service.interfaces;

import java.io.InputStream;

/**
 * Interface for command translator
 */
public interface ICommandTranslator {
    /**
     * Command listener
     * @param input for command sequence
     */
    void listenCommands(InputStream input);
}
