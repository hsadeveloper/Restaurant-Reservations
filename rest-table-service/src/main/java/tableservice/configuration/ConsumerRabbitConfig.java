package tableservice.configuration;

import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import tableservice.ReservationStatus;
import tableservice.TableDefinitionService;
import tableservice.api.ReservationRequest;
import tableservice.api.ReservationResponse;
import tableservice.api.TableAvailabilityRequest;

@Component
class ConsumerRabbitConfig {


  private static final Logger logger = LoggerFactory.getLogger(ConsumerRabbitConfig.class);

  // 1. Declare the Service dependency
  private final TableDefinitionService tableDefinitionService;

  // 2. Inject the Service via Constructor Injection
  public ConsumerRabbitConfig(TableDefinitionService tableDefinitionService) {
    this.tableDefinitionService = tableDefinitionService;
  }
  /*
   * For processCreationRequest-in-0 to mean anything, you need a @Bean method literally named
   * processCreationRequest (a Function or Consumer) — Spring Cloud Stream derives the -in-0/-out-0
   * suffixes automatically from the bean's method name.
   */

  @Bean
  public Function<Message<ReservationRequest>, Message<ReservationResponse>> processCreationRequest() {
    return requestMessage -> {
      // 1. Extract the typed payload directly (Spring handles JSON conversion automatically)
      ReservationRequest requestPayload = requestMessage.getPayload();

      try {
        logger.info("Processing processCreationRequest ..........", requestPayload.toString());

        // 2. Map incoming fields from Event DTO to Service Request format
        TableAvailabilityRequest availabilityRequest =
            new TableAvailabilityRequest(requestPayload.getCustomerId(), requestPayload.getDate(),
                requestPayload.getTime(), requestPayload.getPartySize());

        // 3. Process availability (Insert your internal service logic here)
        ReservationResponse responsePayload = new ReservationResponse();
        responsePayload.setStatus(ReservationStatus.PENDING.toString());
        responsePayload.setSize(availabilityRequest.getPartySize());
        ReservationResponse response =
            tableDefinitionService.checkAvailability(availabilityRequest);

        // 4. CRITICAL: Copy standard and routing headers (replyTo & correlationId) back into
        // response
        return MessageBuilder.withPayload(response).copyHeaders(requestMessage.getHeaders())
            .build();

      } catch (Exception e) {
        logger.error(" [❌] Error processing reservation check availability for ID: {}",
            requestPayload.toString(), e);

        // 5. Build an error payload matching the expected functional output type
        ReservationResponse errorResponse = new ReservationResponse();
        errorResponse.setStatus("REJECTED");

        return MessageBuilder.withPayload(errorResponse).copyHeaders(requestMessage.getHeaders())
            .build();
      }
    };
  }

  //

  //
  // // 3. Changed return type to 'ReservationResponse' for Request-Reply support
  // @RabbitListener(queues = "${rabbitmq.queue.name}",
  // containerFactory = "rabbitListenerContainerFactory")
  // ReservationResponse consume(TableAvailabilityRequest request,
  // @Header(value = "request-type", required = false) String requestType) {
  //
  // logger
  // .info(" [x] Rabbitmq Consumer Received Payload: from Order service " + request.toString());
  // logger.info(" [x] Header 'request-type': " + requestType);
  //
  // if (requestType == null) {
  // logger.warn(
  // " [⚠] Missing 'request-type' header on incoming message. Proceeding with default handling.");
  // } else if ("AVAILABILITY_CHECK".equalsIgnoreCase(requestType)) {
  // logger.info(" [x] Processing as AVAILABILITY_CHECK request");
  // } else if ("table-booking".equalsIgnoreCase(requestType)) {
  // logger.info(" [x] Processing as table-booking request");
  // } else {
  // logger.warn(" [⚠] Unrecognized request-type: " + requestType);
  // }
  //

  // // 5. Invoke the checkAvailability method
  // logger.info(" [x] Invoking checkAvailability logic inside service layer...");
  // ReservationResponse response = tableDefinitionService.checkAvailability(availabilityRequest);
  //
  // logger.info(" [x] Service executed successfully. Response status: " + response.toString());
  //
  // // 6. Return the response; Spring AMQP routes this directly back to the sender
  // return response;
  //
  // } catch (Exception e) {
  // logger.error(" [❌] Error processing reservation check availability (request-type="
  // + requestType + "): ", e);
  //
  // ReservationResponse errorResponse = new ReservationResponse();
  // errorResponse.setStatus("REJECTED");
  // return errorResponse;
  // }
  // }
  //
  // private ReservationResponse CheckTableAvailability(TableAvailabilityRequest request,
  // String requestType) {
  // try {
  // // 4. Map incoming fields from Event DTO to Service Request format if they differ
  // TableAvailabilityRequest availabilityRequest = new TableAvailabilityRequest();
  // availabilityRequest.setCustomerId(request.getCustomerId());
  // availabilityRequest.setDate(request.getDate());
  // availabilityRequest.setTime(request.getTime());
  // availabilityRequest.setPartySize(request.getPartySize());
  //
  // // 5. Invoke the checkAvailability method
  // logger.info(" [x] Invoking checkAvailability logic inside service layer...");
  // ReservationResponse response = tableDefinitionService.checkAvailability(availabilityRequest);
  //
  // logger.info(" [x] Service executed successfully. Response status: " + response.toString());
  //
  // // 6. Return the response; Spring AMQP routes this directly back to the sender
  // return response;
  //
  //
  // }
}
