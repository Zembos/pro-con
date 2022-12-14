package org.zembos.procon.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CommandTranslatorServiceTest {

    CommandProducerService commandProducerService = Mockito.mock(CommandProducerService.class);

    private final CommandTranslatorService commandTranslatorService = new CommandTranslatorService(commandProducerService);

    @Test
    void testSuccessListenCommands() {
        commandTranslatorService.listenCommands(new ByteArrayInputStream("DeleteAll".getBytes()));
        commandTranslatorService.listenCommands(new ByteArrayInputStream("PrintAll".getBytes()));
        commandTranslatorService.listenCommands(new ByteArrayInputStream("Add (1, \"guid12\", \"Miso\")".getBytes()));
        Mockito.verify(commandProducerService, Mockito.times(3)).produceCommand(any());
    }

    @Test
    void testInvalidCommandListenCommands() {
        commandTranslatorService.listenCommands(new ByteArrayInputStream("invalidCommand".getBytes()));
        Mockito.verifyNoInteractions(commandProducerService);
    }

    @Test
    void testInvalidNumberCommandArgumentListenCommands() {
        commandTranslatorService.listenCommands(new ByteArrayInputStream("Add (invalidNumber, \"guid12\", \"Miso\")".getBytes()));
        Mockito.verifyNoInteractions(commandProducerService);
    }

    @Test
    void testInvalidNumOfArgsCommandArgumentListenCommands() {
        commandTranslatorService.listenCommands(new ByteArrayInputStream("Add (1, \"guid12\", \"Miso\", \"oneMoreArg\")".getBytes()));
        Mockito.verifyNoInteractions(commandProducerService);
    }
}