
package orderservice.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import orderservice.entity.ReservationRequestDTO;

@Configuration
public class RedisConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public RedisTemplate<String, List<ReservationRequestDTO>> redisTemplate(
      RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {

    RedisTemplate<String, List<ReservationRequestDTO>> template = new RedisTemplate<>();

    template.setConnectionFactory(connectionFactory);

    // Redis key serializer
    template.setKeySerializer(new StringRedisSerializer());

    // Redis value serializer
    GenericJackson2JsonRedisSerializer serializer =
        new GenericJackson2JsonRedisSerializer(objectMapper);

    template.setValueSerializer(serializer);

    template.setHashKeySerializer(new StringRedisSerializer());

    template.setHashValueSerializer(serializer);

    template.afterPropertiesSet();

    return template;
  }
}

