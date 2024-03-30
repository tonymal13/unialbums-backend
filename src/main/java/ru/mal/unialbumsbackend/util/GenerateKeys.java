package ru.mal.unialbumsbackend.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

public class GenerateKeys {

    public static void main(String[] args) {
//        String key=generateKey();
//        System.out.println(key);

//        String key=generateKey();
//        System.out.println(key);
//        SecretKey key1=generateKey();
    }

//    private static String generateKey() {
//        return Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
//    }

    @Bean
    private SecretKey generateKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

//    public static SecretKey generateKey() {
//        // Генерация секретного ключа для алгоритма HS512
//        return Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
//    }

//    private static String generateKey() {
//        SecureRandom random = new SecureRandom();
//        byte[] bytes = new byte[50]; // 36 bytes * 8 = 288 bits, a little bit more than
//        // the 256 required bits
//        random.nextBytes(bytes);
//        var encoder = Base64.getUrlEncoder().withoutPadding();
//        return encoder.encodeToString(bytes);
//    }

}
