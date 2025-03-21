package com.example.personalnewsfeed.member.dto;

import lombok.Getter;

@Getter
public class MyProfileResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final Long age;
    private final String phoneNumber;
    private final String address;

    public MyProfileResponseDto(Long id, String email, String name, Long age, String phoneNumber, String address) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
