package com.example.personalnewsfeed.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdatePasswordDto {

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}", message = "비밀번호는 8자이상 영문 대소문자, 숫자, 특수문자가 포합되어 있습니다.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String oldPassword;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}", message = "비밀번호는 8자이상 영문 대소문자, 숫자, 특수문자를 사용하세요.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String newPassword;

}
