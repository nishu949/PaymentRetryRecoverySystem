package payment_retry_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class PaymentRetrySystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				PaymentRetrySystemApplication.class,
				args
		);

	}

}
