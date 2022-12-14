package org.zembos.procon.service;

import lombok.extern.slf4j.Slf4j;
import org.zembos.procon.db.command.DBCommand;
import org.zembos.procon.model.User;
import org.zembos.procon.service.interfaces.ICommandProducerService;
import org.zembos.procon.service.interfaces.ICommandTranslator;

import java.io.InputStream;
import java.util.Scanner;

import static org.zembos.procon.db.command.DBPoisonPill.POISON_PILL;

@Slf4j
public class CommandTranslatorService implements ICommandTranslator {
    private final ICommandProducerService commandProducerService;

    public CommandTranslatorService(ICommandProducerService commandProducerService) {
        this.commandProducerService = commandProducerService;
    }

    @Override
    public void listenCommands(InputStream input) {
        try (Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                translateCommand(scanner.nextLine());
            }
        } finally {
            for (int i = 0; i < CommandConsumerService.numOfConsumers.get(); i++) {
                commandProducerService.produceCommand(POISON_PILL);
            }
        }
    }

    private void translateCommand(String commandLine) {
        var trimmedLine = commandLine.trim();
        switch (trimmedLine) {
            case "PrintAll" -> commandProducerService.produceCommand(new DBCommand(DBCommand.Type.SELECT, null));
            case "DeleteAll" -> commandProducerService.produceCommand(new DBCommand(DBCommand.Type.DELETE, null));
            default -> {
                if (trimmedLine.startsWith("Add (") && trimmedLine.endsWith(")")) {
                    buildAddCommand(trimmedLine.substring(trimmedLine.indexOf("(") + 1, trimmedLine.lastIndexOf(")")));
                } else {
                    log.error("Unknown command: {}", commandLine);
                }
            }
        }
    }

    private void buildAddCommand(String arguments) {
        String[] args = arguments.split(", ");
        if (args.length == 3) {
            try {
                int id = Integer.parseInt(args[0]);
                String guid = parseStringArgument(args[1]);
                String name = parseStringArgument(args[2]);
                User user = new User(id, guid, name);
                commandProducerService.produceCommand(new DBCommand(DBCommand.Type.INSERT, user));
            } catch (IllegalArgumentException e) {
                log.error("Invalid arguments {}", arguments);
            }
        } else {
            log.error("Incorrect number of arguments (it will be some validation exception)");
        }
    }

    private String parseStringArgument(String argument) {
        if (argument.startsWith("\"") && argument.endsWith("\"")) {
            return argument.substring(1, argument.length() - 1);
        } else throw new IllegalArgumentException();
    }
}
