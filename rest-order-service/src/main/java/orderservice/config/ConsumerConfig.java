package orderservice.config;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.hateoas.Link;
import org.springframework.messaging.Message;
import orderservice.entity.ReservationResponse;

@Configuration
public class ConsumerConfig {

  private static final Logger logger = LoggerFactory.getLogger(ConsumerConfig.class);

  @Autowired
  private ReservationReplyTracker replyTracker;

  // 1. Inject the auto-configured RedisTemplate
  private final RedisTemplate<String, Object> redisTemplate;


  public ConsumerConfig(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Bean
  public Consumer<Message<ReservationResponse>> processBrief() {
    return message -> {
      String correlationId = message.getHeaders().get("correlationId", String.class);
      ReservationResponse response = message.getPayload();
      response.add(Link.of("/reservations/" + response.getId()).withSelfRel());
      replyTracker.complete(correlationId, response);
    };
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
        redisTemplate.opsForValue().set("tables:all-available", tables, 35, TimeUnit.MINUTES);


      } catch (Exception e) {
        logger.error("Error processing incoming table availability data", e);
      }
    };
  }
}
