package com.example.spring.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestDTO implements Serializable {
    private String email;

    private String pwd;

    private String role;
}
