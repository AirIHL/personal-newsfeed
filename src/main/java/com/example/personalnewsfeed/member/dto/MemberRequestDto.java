package com.example.personalnewsfeed.member.dto;

import lombok.Getter;

@Getter
public class MemberRequestDto {

    private String email;
    private String name;
    private Long age;
    private String phoneNumber;
    private String address;
}
