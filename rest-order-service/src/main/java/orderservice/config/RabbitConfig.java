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
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import orderservice.entity.ReservationResponse;
import orderservice.entity.TableAvailabilityRequest;
import orderservice.repository.ReservationRepository;


@Configuration
public class RabbitConfig {

  private final ReservationRepository reservationRepository;

  public static final String QUEUE_NAME = "restaurant.queue";
  public static final String EXCHANGE_NAME = "restaurant.exchange";
  public static final String ROUTING_KEY = "restaurant.created";

  RabbitConfig(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

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

  @Bean
  public MessageConverter jsonMessageConverter() {
    System.out.println("inside sender message converter");
    JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter("*");
    DefaultClassMapper classMapper = new DefaultClassMapper();
    classMapper.setTrustedPackages("*");

    Map<String, Class<?>> idClassMap = new HashMap<>();
    idClassMap.put("table-request", TableAvailabilityRequest.class);
    idClassMap.put("reservation-response", ReservationResponse.class);

    // ✅ CRITICAL FALLBACK: If the receiver sends its raw class string, intercept and force-map it
    // here
    idClassMap.put("tableservice.api.ReservationResponse", ReservationResponse.class);

    classMapper.setIdClassMapping(idClassMap);
    converter.setClassMapper(classMapper);
    return converter;
  }

}
