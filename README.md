spring.cloud.stream.bindings.<bindingName>.<property>=<value>. The <bindingName>

spring.cloud.stream.bindings.<binding name>.destination=myExchange

spring.cloud.stream.bindings.<binding name>.group=myQueue


spring.cloud.stream.bindings.processCreationRequest-in-0.destination=table--exchange

Spring Cloud Stream uses functional programming. It automatically looks for a Java bean in your code with this exact name

```
@Bean
public Consumer<String> processCreationRequest() {
    return message -> {
        System.out.println("Received message: " + message);
    };
}


 Enables Load Balancing (Competing Consumers)
If you scale up your application and run multiple instances of your microservice:
They will all share this exact same queue.RabbitMQ will round-robin the incoming messages between the instances.Result:
 Each message is processed exactly once by only one instance, preventing duplicate work.


```

The -in- segment tells Spring that this is an inbound binding.
This means your application is acting as a consumer (or listener).
It will actively listen for incoming data rather than sending data out (-out-).Step 3: Map the Index (-0)
The -0 represents the index of the input parameter.Standard Java functions only have one input, so it defaults to 0.


The .destination property tells the binder the exact name of the source data pool in your message broker.
When your Spring Boot application starts up, it communicates with your message broker and says: "Please hook up my processCreationRequest function so it receives everything sent to the destination named table--exchange.

The config property table--exchange strongly indicates that you are using RabbitMQ as your message broker.

RabbitMQ uses a core component called an "Exchange" to receive messages from producers and route them to specific queues.

