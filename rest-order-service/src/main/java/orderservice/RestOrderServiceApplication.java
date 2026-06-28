package orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class RestOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestOrderServiceApplication.class, args);
	}

}
