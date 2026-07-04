package tableservice.configuration;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import tableservice.api.ReservationEventDTO;

@Configuration
public class ConsumerRabbitConfig {

  @RabbitListener(queues = RabbitQueueConfig.QUEUE_NAME,
      containerFactory = "rabbitListenerContainerFactory")
  public void consume(ReservationEventDTO request) {
    System.out.println(" [x] Modern consumer received------: " + request.toString());
  }
}
