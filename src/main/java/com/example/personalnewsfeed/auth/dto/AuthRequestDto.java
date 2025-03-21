package com.example.personalnewsfeed.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRequestDto {

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    private String name;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}", message = "비밀번호는 8자이상 영문 대소문자, 숫자, 특수문자를 사용하세요.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    private Long age;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String phoneNumber;

    private String address;

}
