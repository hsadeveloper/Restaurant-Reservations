// package tableservice.configuration;
//
// import org.springframework.amqp.core.AmqpAdmin;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;
// import org.springframework.amqp.rabbit.core.RabbitAdmin;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class RabbitStreamConfig {
//
//
//
// @Bean
// public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
// RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
// rabbitAdmin.setAutoStartup(true);
// return rabbitAdmin;
// }
// }
