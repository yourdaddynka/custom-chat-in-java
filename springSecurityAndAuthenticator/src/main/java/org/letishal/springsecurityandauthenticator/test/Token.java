//package org.letishal.springsecurityandauthenticator.test;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.UUID;
//
//@NoArgsConstructor
//@AllArgsConstructor
//public class Token {
//    private UUID id;
//    private String subject;
//    private Instant expiresAt;
//    private List<String> authorities;
//    private Instant createdAt;
//
//    public Token(UUID id, String subject, Instant expiresAt, List<String> authorities, Instant createdAt) {
//        this.id = id;
//        this.subject = subject;
//        this.expiresAt = expiresAt;
//        this.authorities = authorities;
//        this.createdAt = createdAt;
//    }
//}