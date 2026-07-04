// package tableservice.configuration;
//
// import java.util.List;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.amqp.core.AmqpAdmin;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component; // FIXED: Use @Component instead of
// import tableservice.AvailabilityRepositoryPort;
// import tableservice.api.ReservationResponse;
//
// @Component // Changed to @Component since this executes application logic, not infrastructure
//// bean
//// definitions
// public class RabbitProducer {
//
// private static final Logger logger = LoggerFactory.getLogger(RabbitProducer.class);
//
// private final RabbitTemplate rabbitTemplate;
// private final AvailabilityRepositoryPort availRepositoryPort;
// private final AmqpAdmin amqpAdmin;
//
// public RabbitProducer(RabbitTemplate rabbitTemplate,
// AvailabilityRepositoryPort availRepositoryPort, AmqpAdmin amqpAdmin) {
// this.rabbitTemplate = rabbitTemplate;
// this.availRepositoryPort = availRepositoryPort;
// this.amqpAdmin = amqpAdmin;
// }
//
// @Scheduled(fixedRate = 60000)
// public void broadcastAvailableTables() {
// List<ReservationResponse> availableTables = availRepositoryPort.checkAvailability();
//
// if (availableTables.isEmpty()) {
// logger.info("No available tables to broadcast");
// return;
// }
//
// try {
// // FIXED: Captured the integer return value from purgeQueue to fix the empty {} log token
// amqpAdmin.purgeQueue(RabbitQueueConfig.QUEUE_NAME, false);
// logger.info("🧹 Programmatic clean: Purged {} stale legacy messages from queue.");
// } catch (Exception e) {
// logger.warn("Could not purge queue on this pass: {}", e.getMessage());
// }
//
// logger.info("Broadcasting batch array of {} available tables to exchange",
// availableTables.size());
//
// // This sends a JSON Array format to RabbitMQ
// rabbitTemplate.convertAndSend(RabbitQueueConfig.EXCHANGE_NAME, RabbitQueueConfig.ROUTING_KEY,
// availableTables);
// }
// }
