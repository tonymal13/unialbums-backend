package ru.mal.unialbumsbackend;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UnialbumsBackendApplication {

	@Value("${spring.minio.url}")
	private String minioUrl;

	public static void main(String[] args) {
		SpringApplication.run(UnialbumsBackendApplication.class, args);
	}


//	@Bean
//	public MinioClient minioClient(){
//		return MinioClient.builder()
//				.endpoint(minioUrl)
//				.credentials("admin","password")
//				.build();
//	}

}
