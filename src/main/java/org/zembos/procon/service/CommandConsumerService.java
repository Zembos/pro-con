package org.zembos.procon.service;

import lombok.extern.slf4j.Slf4j;
import org.zembos.procon.db.command.DBCommand;
import org.zembos.procon.db.command.DBPoisonPill;
import org.zembos.procon.db.DBProcessor;
import org.zembos.procon.db.command.IDBCommand;
import org.zembos.procon.service.interfaces.ICommandConsumerService;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CommandConsumerService implements ICommandConsumerService {

    public static AtomicInteger numOfConsumers = new AtomicInteger(0);

    private final LinkedBlockingDeque<IDBCommand> commandTopic;

    private final DBProcessor dbProcessor = new DBProcessor();

    public CommandConsumerService(LinkedBlockingDeque<IDBCommand> commandTopic) {
        this.commandTopic = commandTopic;
    }

    @Override
    public void consumeCommands() {
        numOfConsumers.incrementAndGet();
        while (true) {
            try {
                IDBCommand command = commandTopic.take();
                if (command instanceof DBCommand dbCommand) {
                    dbProcessor.processDBCommand(dbCommand);
                } else if (command instanceof DBPoisonPill) {
                    log.warn("Received poison pill. Stopping the consumer");
                    numOfConsumers.decrementAndGet();
                    break;
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                numOfConsumers.decrementAndGet();
                break;
            }
        }
    }
}
