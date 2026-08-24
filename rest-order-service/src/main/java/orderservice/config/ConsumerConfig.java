package orderservice.config;

import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import orderservice.entity.ReservationResponse;

@Configuration
public class ConsumerConfig {

  private static final Logger logger = LoggerFactory.getLogger(ConsumerConfig.class);

  private final StreamBridge streamBridge;

  public ConsumerConfig(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }


  @Bean
  public Consumer<List<ReservationResponse>> consumeAvailableTables() {
    return tables -> {
      try {
        logger.info("Received {} available tables from RabbitMQ.", tables.size());


        // Process the received data (e.g., save to local DB, update Redis cache)
        for (ReservationResponse table : tables) {
          logger.debug("Processing table configuration ID: {}", table.getId());
        }
        // Overwrites the entire list in Redis in one single operation
        // redisTemplate.opsForValue().set("tables:all-available", tables, 35, TimeUnit.MINUTES);


      } catch (Exception e) {
        logger.error("Error processing incoming table availability data", e);
      }
    };
  }
}
