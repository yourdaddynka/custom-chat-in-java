package org.letishal.springsecurityandauthenticator.models.dto;

import jakarta.persistence.Column;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class RegistrationUserDto {
   private String clientUsername;
   private char[] userPassword;
   private  String email;
}
