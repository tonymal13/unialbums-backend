package ru.mal.unialbumsbackend.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

public class GenerateKeys {

    public static void main(String[] args) {

    }

    @Bean
    private SecretKey generateKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

}
