package org.zembos.procon.service;

import lombok.extern.slf4j.Slf4j;
import org.zembos.procon.db.command.IDBCommand;
import org.zembos.procon.service.interfaces.ICommandProducerService;

import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
public class CommandProducerService implements ICommandProducerService {
    private final LinkedBlockingDeque<IDBCommand> commandTopic;

    public CommandProducerService(LinkedBlockingDeque<IDBCommand> commandTopic) {
        this.commandTopic = commandTopic;
    }

    @Override
    public void produceCommand(IDBCommand command) {
        try {
            log.info("Registering command {}", command);
            commandTopic.put(command);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
