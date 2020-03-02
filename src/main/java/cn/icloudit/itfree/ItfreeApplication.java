package cn.icloudit.itfree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
@EnableCaching
public class ItfreeApplication extends WebMvcConfigurationSupport {

	public static void main(String[] args) {
		SpringApplication.run(ItfreeApplication.class, args);
	}


}
