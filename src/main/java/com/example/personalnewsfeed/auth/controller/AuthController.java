package com.example.personalnewsfeed.auth.controller;

import com.example.personalnewsfeed.auth.dto.AuthRequestDto;
import com.example.personalnewsfeed.auth.dto.AuthResponseDto;
import com.example.personalnewsfeed.auth.dto.SigninRequestDto;
import com.example.personalnewsfeed.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("auth/signup")
    public void signup(
            @Valid @RequestBody AuthRequestDto requestDto
            ) {
        authService.signup(requestDto);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<AuthResponseDto> signin(
            @Valid @RequestBody SigninRequestDto requestDto
    ) {
        return ResponseEntity.ok(authService.signin(requestDto));
    }

}
