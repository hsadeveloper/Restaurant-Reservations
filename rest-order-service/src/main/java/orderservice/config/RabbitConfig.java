package orderservice.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  public static final String QUEUE_NAME = "restaurant.queue";
  public static final String EXCHANGE_NAME = "restaurant.exchange";
  public static final String ROUTING_KEY = "restaurant.created";

  public static final String QUEUE_NAME_2 = "order.queue";
  public static final String EXCHANGE_NAME_2 = "order.exchange";
  public static final String ROUTING_KEY_2 = "order.created";

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
    // Uses modern Jackson2 implementation compatible with Spring Boot 4
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    DefaultClassMapper classMapper = new DefaultClassMapper();
    classMapper.setTrustedPackages("*");

    Map<String, Class<?>> idClassMap = new HashMap<>();
    idClassMap.put("ReservationResponse", orderservice.entity.ReservationResponse.class);

    // FIXED: Using setIdClassMap exclusively
    classMapper.setIdClassMapping(idClassMap);

    converter.setClassMapper(classMapper);
    return converter;
  }

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
