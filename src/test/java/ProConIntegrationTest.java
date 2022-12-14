import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zembos.procon.db.command.DBPoisonPill;
import org.zembos.procon.db.command.IDBCommand;
import org.zembos.procon.service.CommandConsumerService;
import org.zembos.procon.service.CommandProducerService;
import org.zembos.procon.service.CommandTranslatorService;
import org.zembos.procon.service.interfaces.ICommandConsumerService;
import org.zembos.procon.service.interfaces.ICommandProducerService;

import java.io.ByteArrayInputStream;
import java.util.concurrent.LinkedBlockingDeque;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class ProConIntegrationTest {

    private final LinkedBlockingDeque<IDBCommand> commandTopic = new LinkedBlockingDeque<>(3);

    private final ICommandProducerService commandProducerService = Mockito.spy(new CommandProducerService(commandTopic));

    private final ICommandConsumerService commandConsumerService = new CommandConsumerService(commandTopic);
    private final ICommandConsumerService commandConsumerService2 = new CommandConsumerService(commandTopic);
    private final ICommandConsumerService commandConsumerService3 = new CommandConsumerService(commandTopic);

    @Test
    public void integrationTest() {
        Thread consumerThread = new Thread(commandConsumerService::consumeCommands);
        consumerThread.start();

        String input = """
                Add (1, "a1", "Robert")
                Add (2, "a2", "Martin")
                PrintAll
                DeleteAll
                PrintAll""";

        new CommandTranslatorService(commandProducerService).listenCommands(new ByteArrayInputStream(input.getBytes()));
        // 6 because of Poison pill
        Mockito.verify(commandProducerService, Mockito.times(6)).produceCommand(any());
        Mockito.verify(commandProducerService, Mockito.times(1)).produceCommand(DBPoisonPill.POISON_PILL);
    }

    @Test
    public void integrationTestMultipleConsumers() {
        Thread consumerThread = new Thread(commandConsumerService::consumeCommands);
        consumerThread.start();
        Thread consumerThread2 = new Thread(commandConsumerService2::consumeCommands);
        consumerThread2.start();
        Thread consumerThread3 = new Thread(commandConsumerService3::consumeCommands);
        consumerThread3.start();

        String input = """
                Add (1, "a1", "Robert")
                Add (2, "a2", "Martin")
                PrintAll
                DeleteAll
                PrintAll""";

        new CommandTranslatorService(commandProducerService).listenCommands(new ByteArrayInputStream(input.getBytes()));
        Mockito.verify(commandProducerService, Mockito.times(8)).produceCommand(any());
        Mockito.verify(commandProducerService, Mockito.times(3)).produceCommand(DBPoisonPill.POISON_PILL);
    }
}
