package com.example.jsoup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class JsoupApplication {
	private static final Logger log = LoggerFactory.getLogger(JsoupApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(JsoupApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/jsoup").allowedOrigins(("*")).allowedHeaders("*");
				registry.addMapping("/comments").allowedOrigins("*").allowedHeaders("*");
				registry.addMapping("/2fa").allowedOrigins("*").allowedHeaders("*");
			}
		};
	}
//
//	@Bean
//	public CommandLineRunner demo(CommentRepository repository) {
//		return (args) -> {
//			// save a few customers
//			// fetch all customers
//			log.info("Comments found with findAll():");
//			log.info("-------------------------------");
//			for (Comment comment : repository.findAll()) {
//				log.info(comment.toString());
//			}
//			log.info("");
//
//			// fetch an individual customer by ID
//			Comment comment = repository.findById(1L);
//			log.info("Comment found with findById(1L):");
//			log.info("--------------------------------");
//			log.info(comment.toString());
//			log.info("");
//
//			// fetch customers by last name
//			log.info("Comment found with findByComment('Bauer'):");
//			log.info("--------------------------------------------");
//			repository.findByComment("Jack").forEach(jack -> {
//				log.info(jack.toString());
//			});
//			// for (Customer bauer : repository.findByLastName("Bauer")) {
//			// 	log.info(bauer.toString());
//			// }
//			log.info("");
//		};
//	}

}
