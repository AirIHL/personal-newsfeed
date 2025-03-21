package com.example.personalnewsfeed.member.entity;

import com.example.personalnewsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    @Column(nullable = false)
    private String password;

    private Long age;

    private String phoneNumber;

    private String address;

    public Member(String email, String name, String password, Long age, String phoneNumber, String address) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void update(String email, String name, Long age, String phoneNumber, String address) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}