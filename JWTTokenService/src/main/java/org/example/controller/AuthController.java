package org.example.controller;

import org.example.exception.CustomException;
import org.example.model.User;
import org.example.model.dto.UserDto;
import org.example.response.ResponseMessage;
import org.example.response.ResponseMessageObject;
import org.example.security.jwt.JwtTokenProvider;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

import static java.lang.System.out;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("register/phone")
    public ResponseEntity<ResponseMessage> register(@RequestParam String login, @RequestParam char[] password,
                                                    @RequestParam String role, @RequestParam String phone,
                                                    @RequestParam int code) throws CustomException {
        String token = jwtTokenProvider.generateToken(login, role);
        User user = new User(login, password, role, token, phone);
        try {
            userService.save(user, code);
            return ResponseEntity.ok().body(
                    new ResponseMessageObject("Success", null, 200, token,
                            new UserDto(user.getLogin(), user.getRole(), user.getPhone())));
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    @PostMapping("login")
    public ResponseEntity<ResponseMessage> login(@RequestParam String login, @RequestParam char[] password) throws CustomException {
        User user = userService.findByLogin(login);
        try {
            User.PasswordUtils.matches(password, user.getPassword());
            return ResponseEntity.ok().body(
                    new ResponseMessageObject("Success", null, 200, user.getToken(),
                            new UserDto(user.getLogin(), user.getRole(), user.getPhone())));
        } finally {
            Arrays.fill(password, '\0');
        }
    }


}
