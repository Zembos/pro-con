package org.zembos.procon.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CommandTranslatorServiceTest {

    private static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of("invalidCommand"),
                Arguments.of("Add (invalidNumber, \"guid12\", \"Miso\")"),
                Arguments.of("Add (1, \"guid12\", \"Miso\", \"oneMoreArg\")")
        );
    }

    CommandProducerService commandProducerService = Mockito.mock(CommandProducerService.class);

    private final CommandTranslatorService commandTranslatorService = new CommandTranslatorService(commandProducerService);

    @Test
    void testSuccessListenCommands() {
        commandTranslatorService.listenCommands(new ByteArrayInputStream("DeleteAll".getBytes()));
        commandTranslatorService.listenCommands(new ByteArrayInputStream("PrintAll".getBytes()));
        commandTranslatorService.listenCommands(new ByteArrayInputStream("Add (1, \"guid12\", \"Miso\")".getBytes()));
        Mockito.verify(commandProducerService, Mockito.times(3)).produceCommand(any());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    void testInvalidCommandAndArgumentsListenCommands(String testCommand) {
        commandTranslatorService.listenCommands(new ByteArrayInputStream(testCommand.getBytes()));
        Mockito.verifyNoInteractions(commandProducerService);
    }
}