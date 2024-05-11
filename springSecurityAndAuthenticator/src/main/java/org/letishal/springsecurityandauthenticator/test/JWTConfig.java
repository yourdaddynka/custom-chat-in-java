//package org.letishal.springsecurityandauthenticator.test;
//
//import org.letishal.springsecurityandauthenticator.test.old.GetCsrfTokenFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.ExceptionTranslationFilter;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
//
//@Configuration
//public class JWTConfig {
//
//    @Bean
//    public TokenCookieJweStringSerializer tokenCookieJweStringSerializer(
//            @Value("${jwt.cookie-token-key}") String cookieTokenKey
//    ) throws Exception {
//        return new TokenCookieJweStringSerializer(new DirectEncrypter(
//                OctetSequenceKey.parse(cookieTokenKey)
//        ));
//    }
//
//    @Bean
//    public TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
//            @Value("${jwt.cookie-token-key}") String cookieTokenKey,
//            JdbcTemplate jdbcTemplate
//    ) throws Exception {
//        return new TokenCookieAuthenticationConfigurer()
//                .tokenCookieStringDeserializer(new TokenCookieJweStringDeserializer(
//                        new DirectDecrypter(
//                                OctetSequenceKey.parse(cookieTokenKey)
//                        )
//                ))
//                .jdbcTemplate(jdbcTemplate);
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http,
//            TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer,
//            TokenCookieJweStringSerializer tokenCookieJweStringSerializer) throws Exception {
//        var tokenCookieSessionAuthenticationStrategy = new TokenCookieSessionAuthenticationStrategy();
//        tokenCookieSessionAuthenticationStrategy.setTokenStringSerializer(tokenCookieJweStringSerializer);
//
//        http.httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults())
//                .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
//                .authorizeHttpRequests(authorizeHttpRequests ->
//                        authorizeHttpRequests
//                                .requestMatchers("/manager.html", "/manager").hasRole("MANAGER")
//                                .requestMatchers("/error", "index.html").permitAll()
//                                .anyRequest().authenticated())
//                .sessionManagement(sessionManagement -> sessionManagement
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                        .sessionAuthenticationStrategy(tokenCookieSessionAuthenticationStrategy))
//                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//                        .sessionAuthenticationStrategy((authentication, request, response) -> {}));
//
//        http.apply(tokenCookieAuthenticationConfigurer);
//
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(JdbcTemplate jdbcTemplate) {
//        return username -> jdbcTemplate.query("select * from t_user where c_username = ?",
//                (rs, i) -> User.builder()
//                        .username(rs.getString("c_username"))
//                        .password(rs.getString("c_password"))
//                        .authorities(
//                                jdbcTemplate.query("select c_authority from t_user_authority where id_user = ?",
//                                        (rs1, i1) ->
//                                                new SimpleGrantedAuthority(rs1.getString("c_authority")),
//                                        rs.getInt("id")))
//                        .build(), username).stream().findFirst().orElse(null);
//    }
//}
