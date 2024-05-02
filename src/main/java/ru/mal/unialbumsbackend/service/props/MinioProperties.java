package ru.mal.unialbumsbackend.service.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
@Getter
@Setter
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
