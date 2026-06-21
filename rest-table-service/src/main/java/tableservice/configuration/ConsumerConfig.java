package tableservice.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.rabbitmq.stream.Consumer;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import tools.jackson.databind.ObjectMapper;


@Configuration
public class ConsumerConfig {

  private static final Logger log = LoggerFactory.getLogger(ConsumerConfig.class);

  @Value("${stream.name:events.super.streams.filtering}")
  private String streamName;


  private final ObjectMapper objectMapper;



  // Spring Boot auto-configures and injects a shared ObjectMapper
  public ConsumerConfig(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }



  /**
   * 2. Subscribes to the stream and reads messages
   */
  @Bean
  public Consumer consumer(Environment environment) {
    environment.streamCreator().stream(streamName).create();
    log.info("Ensured stream '{}' exists", streamName);

    return environment.consumerBuilder().stream(streamName).offset(OffsetSpecification.next())
        .messageHandler((context, message) -> {
          byte[] body = message.getBodyAsBinary();
          log.info("Received {} bytes at offset {}", body.length, context.offset());
        }).build();
  }


}
