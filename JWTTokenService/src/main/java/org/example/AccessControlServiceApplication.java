package org.example;


import org.example.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AccessControlServiceApplication implements CommandLineRunner {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private SecurityRepository securityRepository;
    public static void main(String[] args) {
        SpringApplication.run(AccessControlServiceApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
    }

    private void createAdminUser() {
        String token = jwtTokenProvider.generateToken("1234567890", "ADMIN");
        securityRepository.save(new Security("1234567890", token, "EMPLOYEE"));
    }
}