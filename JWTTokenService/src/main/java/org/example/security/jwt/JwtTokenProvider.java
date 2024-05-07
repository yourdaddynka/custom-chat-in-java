package org.example.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
@Component
public class JwtTokenProvider {

    // Секретный ключ для подписи и проверки JWT токенов
    private final SecretKey secretKey;

    public JwtTokenProvider() {
        // Ваш секретный ключ (можно хранить в конфигурационном файле)
        String secretKeyString = "brPbbhbYGG45DDDHyhgJvfgKIi234";

        // Преобразование строки в секретный ключ
        byte[] secretKeyBytes = secretKeyString.getBytes();
        this.secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // Метод для генерации JWT токена на основе имени пользователя и роли
    public String generateToken(String login, String role) {
        // Установка текущей даты и времени
        Date now = new Date();
        // Рассчет времени истечения срока действия токена (24 часа после текущего времени)
        Date expiryDate = new Date(now.getTime() + 24 * 60 * 60 * 1000);
        // Генерация JWT токена с указанием подлежащего, времени создания и времени истечения срока действия
        return Jwts.builder()
                .claim("role", role) // Добавление роли в токен
                .setSubject(login)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey) // Подписание токена с использованием секретного ключа
                .compact(); // Компактное представление токена в виде строки
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
