package tableservice.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tableservice.TableDefinitionService; // Ensure correct service import
import tableservice.api.ReservationResponse;
import tableservice.api.TableAvailabilityRequest;

@Component
public class ConsumerRabbitConfig {

  private static final Logger logger = LoggerFactory.getLogger(ConsumerRabbitConfig.class);

  // 1. Declare the Service dependency
  private final TableDefinitionService tableDefinitionService;

  // 2. Inject the Service via Constructor Injection
  public ConsumerRabbitConfig(TableDefinitionService tableDefinitionService) {
    this.tableDefinitionService = tableDefinitionService;
  }

  // 3. Changed return type to 'ReservationResponse' for Request-Reply support
  @RabbitListener(queues = RabbitQueueConfig.QUEUE_NAME,
      containerFactory = "rabbitListenerContainerFactory")
  public ReservationResponse consume(TableAvailabilityRequest request) {
    logger.info(" [x] Modern consumer received payload: " + request.toString());

    try {
      // 4. Map incoming fields from Event DTO to Service Request format if they differ
      TableAvailabilityRequest availabilityRequest = new TableAvailabilityRequest();
      availabilityRequest.setCustomerId(request.getCustomerId());
      availabilityRequest.setDate(request.getDate());
      availabilityRequest.setTime(request.getTime());
      availabilityRequest.setPartySize(request.getPartySize());

      // 5. Invoke the checkAvailability method
      logger.info(" [x] Invoking checkAvailability logic inside service layer...");
      ReservationResponse response = tableDefinitionService.checkAvailability(availabilityRequest);

      logger.info(" [x] Service executed successfully. Response status: " + response.toString());

      // 6. Return the response; Spring AMQP routes this directly back to the sender
      return response;

    } catch (Exception e) {
      logger.error(" [❌] Error processing reservation check availability: ", e);

      // Build a fallback failure response back so the sender isn't left hanging on a timeout
      ReservationResponse errorResponse = new ReservationResponse();
      errorResponse.setStatus("REJECTED");
      return errorResponse;
    }
  }
}
