package orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  public static final String QUEUE_NAME = "restaurant.queue";
  public static final String EXCHANGE_NAME = "restaurant.exchange";
  public static final String ROUTING_KEY = "restaurant.created";

  ////  public static final String QUEUE_NAME_2 = "order.queue";
  // public static final String EXCHANGE_NAME_2 = "order.exchange";
  // public static final String ROUTING_KEY_2 = "order.created";

  @Bean
  public Queue ordersQueue() {
    return new Queue(QUEUE_NAME, true);
  }

  @Bean
  public TopicExchange ordersExchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public Binding binding() {
    return BindingBuilder.bind(ordersQueue()).to(ordersExchange()).with(ROUTING_KEY);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    // ✅ FIXED: Using the modern, non-deprecated converter class name
    return new JacksonJsonMessageConverter();
  }

  // ✅ ADD THIS: Safely creates the exchange inside RabbitMQ if it does not exist yet
  // @Bean
  // public TopicExchange secondExchange() {
  // return new TopicExchange(EXCHANGE_NAME_2);
  // }
  //
  // // ✅ ADD THIS: Safely creates the queue inside RabbitMQ if it does not exist yet
  // @Bean
  // public Queue secondQueue() {
  // return new Queue(QUEUE_NAME_2, true);
  // }
  //
  // // ✅ ADD THIS: Binds them together automatically on startup
  // @Bean
  // public Binding secondBinding() {
  // return BindingBuilder.bind(secondQueue()).to(secondExchange()).with(ROUTING_KEY_2);
  // }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jsonMessageConverter());
    return template;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(jsonMessageConverter());
    return factory;
  }
}
