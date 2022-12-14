package org.zembos.procon;

import lombok.extern.slf4j.Slf4j;
import org.zembos.procon.db.command.IDBCommand;
import org.zembos.procon.service.CommandConsumerService;
import org.zembos.procon.service.CommandProducerService;
import org.zembos.procon.service.CommandTranslatorService;
import org.zembos.procon.service.interfaces.ICommandConsumerService;
import org.zembos.procon.service.interfaces.ICommandProducerService;

import java.io.ByteArrayInputStream;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
public class Application {
    public static void main(String[] args) {
        final LinkedBlockingDeque<IDBCommand> commandTopic = new LinkedBlockingDeque<>(3);

        ICommandProducerService commandProducerService = new CommandProducerService(commandTopic);

        ICommandConsumerService commandConsumerService = new CommandConsumerService(commandTopic);
        Thread consumerThread = new Thread(commandConsumerService::consumeCommands);
        consumerThread.start();

        String input = """
                Add (1, "a1", "Robert")
                Add (2, "a2", "Martin")
                PrintAll
                DeleteAll
                PrintAll""";

        new CommandTranslatorService(commandProducerService).listenCommands(new ByteArrayInputStream(input.getBytes()));

        // If you want to play with console input
        //new CommandTranslatorService(commandProducerService).listenCommands(System.in);
    }
}