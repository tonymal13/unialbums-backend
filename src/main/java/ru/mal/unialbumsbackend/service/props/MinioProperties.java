package ru.mal.unialbumsbackend.service.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Getter
@Setter
//@ConfigurationProperties(prefix = "spring.minio")
public class MinioProperties {

    @Value("${spring.minio.url}")
    private String url;

    @Value("${spring.minio.bucket}")
    private String bucket;

    @Value("${spring.minio.access-key}")
    private String accessKey;
    @Value("${spring.minio.secret-key}")
    private String secretKey;

}
