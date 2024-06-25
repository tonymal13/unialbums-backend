package ru.mal.unialbumsbackend.service.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mal.unialbumsbackend.service.props.MinioProperties;

@Configuration
@Data
public class MinioConfig {

    private MinioProperties minioProperties;
    @Bean
	public MinioClient minioClient(){
		return MinioClient.builder()
				.endpoint(minioProperties.getUrl())
				.credentials("admin","password")
				.build();
	}

}
