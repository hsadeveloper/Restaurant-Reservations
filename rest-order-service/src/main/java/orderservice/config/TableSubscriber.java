package orderservice.config;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import orderservice.entity.ReservationRequestDTO;

@Component
public class TableSubscriber {

  private static final Logger log = LoggerFactory.getLogger(TableSubscriber.class);

  @Autowired
  private RedisTemplate<String, String> stringRedisTemplate;

  private final ObjectMapper mapper;

  public TableSubscriber() {
    this.mapper = new ObjectMapper();
    this.mapper.registerModule(new JavaTimeModule());
    this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  // 1. BOOTSTRAP HOOK: Runs a query the exact second the app starts up so it doesn't miss old data
  @EventListener(ApplicationReadyEvent.class)
  public void bootstrapCacheOnStartup() {
    log.info("Order-service initialized. Running initial boot-strapping sync query...");
    // Reuses the parsing logic by simulating a mock 'set' action
    this.handleMessage("set", "__keyspace@0__:cached-available-tables");
  }

  // 2. KEYSPACE LISTENER: Automatically handles live real-time updates from Redis
  public void handleMessage(String action, String channel) {
    try {
      log.info("Sync event evaluated. Action: {} on Key channel: {}", action, channel);

      // Only run parsing logic if a key is added or modified
      if (!"set".equalsIgnoreCase(action)) {
        return;
      }

      // Fetch the updated payload directly from the source key
      String realJsonPayload = stringRedisTemplate.opsForValue().get("cached-available-tables");

      if (realJsonPayload == null) {
        log.warn("Target data key 'cached-available-tables' evaluated to null or missing.");
        return;
      }

      // Safe Jackson extraction process
      ReservationRequestDTO[] tables =
          mapper.readValue(realJsonPayload, ReservationRequestDTO[].class);
      int count = (tables != null) ? tables.length : 0;
      log.info("Successfully synchronized cache state! Tracked array items: {}", count);

      // Back up the JSON string to your local internal key configuration
      stringRedisTemplate.opsForValue().set("availableTables::latest", realJsonPayload, 15,
          TimeUnit.MINUTES);

    } catch (Exception e) {
      log.error("Failed to execute sync pipeline operation.", e);
    }
  }
}
