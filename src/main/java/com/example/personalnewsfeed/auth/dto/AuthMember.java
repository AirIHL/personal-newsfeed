package com.example.personalnewsfeed.auth.dto;

import lombok.Getter;

@Getter
public class AuthMember {

    private final long id;
    private final String email;

    public AuthMember(long id, String email) {
        this.id = id;
        this.email = email;
    }
}
