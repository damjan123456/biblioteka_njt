package com.biblioteka.biblioteka_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private Integer id;
    private String email;
    private String role;
    private String message;
    private String token;
}