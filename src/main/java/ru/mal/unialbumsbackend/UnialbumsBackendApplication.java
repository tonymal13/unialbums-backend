package ru.mal.unialbumsbackend;

import io.minio.MinioClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UnialbumsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnialbumsBackendApplication.class, args);
	}


	@Bean
	public MinioClient minioClient(){
		return MinioClient.builder()
//				.endpoint("${spring.minio.url}")
				.endpoint("http://minio:9000")
//				.endpoint("http://localhost:9000")
				.credentials("admin","password")
				.build();
	}

}
