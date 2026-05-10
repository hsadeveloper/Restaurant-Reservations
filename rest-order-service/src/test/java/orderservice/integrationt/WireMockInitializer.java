package orderservice.integrationt;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class WireMockInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        WireMockServer wireMockServer = new WireMockServer(
            WireMockConfiguration.wireMockConfig().dynamicPort()
        );

        wireMockServer.start();

        // Stop WireMock when Spring context closes
        applicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                wireMockServer.stop();
            }
        });

        // Register as a Spring bean so tests can autowire it
        applicationContext.getBeanFactory()
            .registerSingleton("wireMockServer", wireMockServer);

        // Inject the dynamic port into Spring properties
        TestPropertyValues.of(
            "server.port" + wireMockServer.port()
        ).applyTo(applicationContext);
        
    }
}