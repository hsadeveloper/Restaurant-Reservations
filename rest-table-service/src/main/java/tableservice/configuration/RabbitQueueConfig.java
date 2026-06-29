package tableservice.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory; // ✅ ADDED
// IMPORT
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitQueueConfig {

  public static final String QUEUE_NAME = "restaurant.queue";
  public static final String EXCHANGE_NAME = "restaurant.exchange";
  public static final String ROUTING_KEY = "restaurant.created";

  public static final String QUEUE_NAME_2 = "order.queue";
  public static final String EXCHANGE_NAME_2 = "order.exchange";
  public static final String ROUTING_KEY_2 = "order.created";

  // ==========================================
  // 1. FIRST QUEUE & EXCHANGE CONFIGURATION
  // ==========================================
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

  // ==========================================
  // 2. SECOND QUEUE & EXCHANGE CONFIGURATION
  // ==========================================
  @Bean
  public Queue secondQueue() {
    return new Queue(QUEUE_NAME_2, true);
  }

  @Bean
  public TopicExchange secondExchange() {
    return new TopicExchange(EXCHANGE_NAME_2);
  }

  @Bean
  public Binding secondBinding() {
    return BindingBuilder.bind(secondQueue()).to(secondExchange()).with(ROUTING_KEY_2);
  }

  // ==========================================
  // 3. MESSAGE CONVERSION & TEMPLATE CONFIG
  // ==========================================


  // ... Inside your RabbitQueueConfig class ...

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }


  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jsonMessageConverter());
    return template;
  }

  // ==========================================
  // 4. AUTOMATIC LISTENER FACTORY COUPLING
  // ==========================================
  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {

    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);

    // ✅ CRITICAL: Links your JSON mapper to the background @RabbitListener annotation loops
    factory.setMessageConverter(jsonMessageConverter);

    return factory;
  }

  @Override
  public String toString() {
    return "RabbitMQConfig [jsonQueues()=" + secondQueue() + ", exchange()=" + secondExchange()
        + "]";
  }
}
