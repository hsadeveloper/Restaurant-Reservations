package orderservice.config;

import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Link;
import org.springframework.messaging.Message;
import orderservice.entity.ReservationResponse;

@Configuration
public class ReservationReplyConsumerConfig {

  @Autowired
  private ReservationReplyTracker replyTracker;

  @Bean
  public Consumer<Message<ReservationResponse>> processBrief() {
    return message -> {
      String correlationId = message.getHeaders().get("correlationId", String.class);
      ReservationResponse response = message.getPayload();
      response.add(Link.of("/reservations/" + response.getId()).withSelfRel());
      replyTracker.complete(correlationId, response);
    };
  }
}
