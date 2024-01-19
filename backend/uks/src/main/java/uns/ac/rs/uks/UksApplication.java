package uns.ac.rs.uks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class UksApplication {

	public static void main(String[] args) {
		SpringApplication.run(UksApplication.class, args);
	}

}
