package com.example.personalnewsfeed.member.controller;

import com.example.personalnewsfeed.auth.annotation.Auth;
import com.example.personalnewsfeed.auth.dto.AuthMember;
import com.example.personalnewsfeed.member.dto.MemberRequestDto;
import com.example.personalnewsfeed.member.dto.MemberResponseDto;
import com.example.personalnewsfeed.member.dto.MyProfileResponseDto;
import com.example.personalnewsfeed.member.dto.UpdatePasswordDto;
import com.example.personalnewsfeed.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 본인 프로필 조회
    @GetMapping("/members/me")
    public ResponseEntity<MyProfileResponseDto> findMyProfile(@Auth AuthMember authMember) {
        MyProfileResponseDto profile = memberService.findMyProfile(authMember);

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    // 유저 다건 조회
    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> findProfileAll() {
        List<MemberResponseDto> profiles = memberService.findProfileAll();

        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    // 유저 단건 조회
    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponseDto> findProfileById(@PathVariable Long id) {
        MemberResponseDto profile = memberService.findProfileById(id);

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    //자기 자신만 프로필 변경 가능
    @PutMapping("/members")
    public ResponseEntity<Void> update(
            @Auth AuthMember authMember,
            @RequestBody MemberRequestDto requestDto
            ) {
            memberService.update(authMember, requestDto);

            return new ResponseEntity<>(HttpStatus.OK);
    }

    // 비밀번호 변경
    @PutMapping("/members/password")
    public ResponseEntity<Void> updatePassword(@Auth AuthMember authMember, @Valid @RequestBody UpdatePasswordDto passwordDto) {
        memberService.updatePassword(authMember, passwordDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
