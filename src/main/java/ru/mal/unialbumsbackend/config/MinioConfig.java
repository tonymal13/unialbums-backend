package ru.mal.unialbumsbackend.config;

import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mal.unialbumsbackend.service.props.MinioProperties;

@Configuration
@Getter
@Setter
@AllArgsConstructor
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
