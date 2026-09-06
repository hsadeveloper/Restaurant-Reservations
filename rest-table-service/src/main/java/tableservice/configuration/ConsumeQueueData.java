package tableservice.configuration;

import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import tableservice.TableDefinitionService;
import tableservice.api.ReservationRequest;
import tableservice.api.ReservationResponse;
import tableservice.api.TableAvailabilityRequest;

@Component
class ConsumeQueueData {

  private static final Logger logger = LoggerFactory.getLogger(ConsumeQueueData.class);
  private final TableDefinitionService tableDefinitionService;
  private StreamBridge streamBridge;

  public ConsumeQueueData(TableDefinitionService tableDefinitionService,
      StreamBridge streamBridge) {
    super();
    this.tableDefinitionService = tableDefinitionService;
    this.streamBridge = streamBridge;
  }

  @Bean
  public Function<Message<ReservationResponse>, Message<ReservationResponse>> confirmTable(
      TableDefinitionService tableDefinitionService) {
    return requestMessage -> {
      ReservationResponse reservation = requestMessage.getPayload();
      logger.info("Processing confirmTable for reservationId={}", reservation.toString());
      tableDefinitionService.confirmTable(reservation.getTableId());
      ReservationResponse response = new ReservationResponse();
      response.setId(reservation.getTableId());
      response.setStatus("CONFIRMED");
      return MessageBuilder.withPayload(response).copyHeaders(requestMessage.getHeaders())
          .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE).build();
    };
  }

  @Bean
  public Function<Message<ReservationRequest>, Message<ReservationResponse>> processCreationRequest() {
    return requestMessage -> {
      logger.info("Processing processCreationRequest ..........");
      ReservationRequest requestPayload = requestMessage.getPayload();

      try {
        logger.info("Processing processCreationRequest .......... {}", requestPayload);

        TableAvailabilityRequest availabilityRequest = new TableAvailabilityRequest(
            requestPayload.getDate(), requestPayload.getTime(), requestPayload.getPartySize(),
            requestPayload.getCustomerId(), requestPayload.getTableId());

        // Map the reservationId so checkAvailability() can include it in the response
        availabilityRequest.setReservationId(requestPayload.getReservationId());

        ReservationResponse response =
            tableDefinitionService.checkAvailability(availabilityRequest);
        logger.info("Processing processCreationRequest succeeded .......... Table ID: {}",
            response.getTableId());

        return MessageBuilder.withPayload(response).copyHeaders(requestMessage.getHeaders())
            .build();

      } catch (IllegalArgumentException e) {
        logger.warn(" [⚠️] Reservation rejected for request: {} — {}", requestPayload,
            e.getMessage());
        ReservationResponse errorResponse = new ReservationResponse();
        errorResponse.setStatus("REJECTED");
        return MessageBuilder.withPayload(errorResponse).copyHeaders(requestMessage.getHeaders())
            .build();
      } catch (Exception e) {
        logger.error(" [❌] Unexpected error processing reservation for request: {}", requestPayload,
            e);
        throw e;
      }
    };
  }

}
