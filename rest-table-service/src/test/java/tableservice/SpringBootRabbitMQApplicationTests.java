package tableservice;

import static org.assertj.core.api.Assertions.assertThat; // Corrected AssertJ import
import static org.awaitility.Awaitility.waitAtMost;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // Thread-safe collection
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class SpringBootRabbitMQApplicationTests {

  @Container
  @ServiceConnection
  static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:4.0-alpine");

  @Autowired
  private AmqpTemplate amqpTemplate;

  @Autowired
  private TestListener testListener;

  @BeforeEach
  void setUp() {
    // Clear message state before each test execution
    this.testListener.clearMessages();
  }

  @Test
  void consumeMessage() {
    // Send directly to the queue name (default exchange routes by queue name)
    this.amqpTemplate.convertAndSend("test", "test-data");

    waitAtMost(Duration.ofSeconds(10)).untilAsserted(() -> {
      assertThat(this.testListener.getMessages()).hasSize(1).containsExactly("test-data");
    });
  }

  @TestConfiguration
  static class Config {
    @Bean
    TestListener testListener() {
      return new TestListener();
    }
  }

  static class TestListener {
    // MUST be a thread-safe list since it is updated asynchronously
    private final List<String> messages = new CopyOnWriteArrayList<>();

    @RabbitListener(queuesToDeclare = @Queue("test"))
    void listen(String data) {
      this.messages.add(data);
    }

    public List<String> getMessages() {
      return this.messages;
    }

    public void clearMessages() {
      this.messages.clear();
    }
  }
}
