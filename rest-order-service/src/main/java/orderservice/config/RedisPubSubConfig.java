package orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisPubSubConfig {

  // Added a unique name identifier to prevent collision with Spring Session
  @Bean(name = "customRedisContainer")
  public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);

    // Listens to any logical Redis database index for updates to the key
    container.addMessageListener(listenerAdapter,
        new PatternTopic("__keyspace@*__:cached-available-tables"));
    return container;
  }

  @Bean
  public MessageListenerAdapter listenerAdapter(TableSubscriber subscriber) {
    // Routes the keyspace text strings to your TableSubscriber's handleMessage method
    MessageListenerAdapter adapter = new MessageListenerAdapter(subscriber, "handleMessage");

    // Crucial: Forces raw Redis bytes into clean Java Strings for your method parameters
    adapter.setSerializer(new StringRedisSerializer());
    return adapter;
  }
}
