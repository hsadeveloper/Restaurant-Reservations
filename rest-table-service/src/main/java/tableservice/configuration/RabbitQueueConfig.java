package tableservice.configuration;

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
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitQueueConfig {

  public static final String QUEUE_NAME = "restaurant.queue";
  public static final String EXCHANGE_NAME = "restaurant.exchange";
  public static final String ROUTING_KEY = "restaurant.created";

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
    System.out.println("inside receiver message converter");
    JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter("*");
    DefaultClassMapper classMapper = new DefaultClassMapper();
    classMapper.setTrustedPackages("*");

    // Maps incoming string header tags explicitly to your local class entities
    Map<String, Class<?>> idClassMap = new HashMap<>();
    idClassMap.put("table-request", tableservice.api.TableAvailabilityRequest.class);
    idClassMap.put("reservation-response", tableservice.api.ReservationResponse.class);

    // This handles cases where a legacy __TypeId__ package string slips through
    idClassMap.put("orderservice.entity.TableAvailabilityRequest",
        tableservice.api.TableAvailabilityRequest.class);

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

    // FIXED: Call the method explicitly using () to guarantee the custom bean maps into the
    // background listener
    factory.setMessageConverter(jsonMessageConverter());

    return factory;
  }
}
