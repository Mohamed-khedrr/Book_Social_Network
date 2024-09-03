package com.project.book.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {
    @Email(message = "Not a valid email")
    @NotBlank(message = "Email is required")
    @NotEmpty(message = "Email is required")
    private String email ;

    @NotBlank(message = "Password is required")
    @NotEmpty(message = "Password is required")
    private String password ;
}
