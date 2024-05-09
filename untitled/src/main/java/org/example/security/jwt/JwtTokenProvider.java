package org.example.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider() {
        // Ваш секретный ключ (можно хранить в конфигурационном файле)
        String secretKeyString = "2yWX%8FhB1z!KQd@P3mZvfA$LgXnSjWn";

        // Преобразование строки в секретный ключ
        byte[] secretKeyBytes = secretKeyString.getBytes();
        this.secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
    }


    // Метод для извлечения имени пользователя из JWT токена
    public String getUsernameFromToken(String token) {
        // Парсинг и верификация токена с использованием секретного ключа
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
        // Извлечение имени пользователя (подлежащего) из тела токена
        return claims.getBody().getSubject();
    }

    // Метод для извлечения роли из JWT токена
    public String getRoleFromToken(String token) {
        // Парсинг и верификация токена с использованием секретного ключа
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
        // Извлечение роли из тела токена
        return (String) claims.getBody().get("role");
    }
}
