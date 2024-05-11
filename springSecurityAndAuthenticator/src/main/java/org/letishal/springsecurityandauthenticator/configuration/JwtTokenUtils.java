package org.letishal.springsecurityandauthenticator.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.letishal.springsecurityandauthenticator.models.Client;
import org.letishal.springsecurityandauthenticator.models.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@PropertySource("classpath:jwt_prop.properties")
@Configuration
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.lifetime}")
    Duration jwtLifetime;


    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof Client) {
            claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
            claims.put("username", userDetails.getUsername());
            claims.put("email", ((Client) userDetails).getEmail());
            Date issueDate = new Date();
            Date expiredDate = new Date(issueDate.getTime() + jwtLifetime.toMillis());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(issueDate)
                    .setExpiration(expiredDate)
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
        }
        throw new RuntimeException(String.format("Пользователь '%s' не найден", userDetails.getUsername()));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


    public String getUserName(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String getUserEmail(String token) {
        return getAllClaimsFromToken(token).get("email", String.class);
    }

    public List<String> getUserRoles(String token) {
        List<?> roles = getAllClaimsFromToken(token).get("roles", List.class);
        return roles.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return (getUserName(token).equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
