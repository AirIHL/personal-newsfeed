package com.example.personalnewsfeed.follow.controller;

import com.example.personalnewsfeed.auth.annotation.Auth;
import com.example.personalnewsfeed.auth.dto.AuthMember;
import com.example.personalnewsfeed.follow.service.FollowService;
import com.example.personalnewsfeed.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping("/follows/{follow_member_id}")
    public ResponseEntity<Void> follow(@Auth AuthMember authMember, @PathVariable Long follow_member_id) {

        followService.follow(authMember, follow_member_id);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 팔로우하는 회원 정보 목록 조회
    @GetMapping("/follows/following")
    public ResponseEntity<List<MemberResponseDto>> getFollowing(@Auth AuthMember authMember) {
        List<MemberResponseDto> following = followService.getFollowingIds(authMember);

        return new ResponseEntity<>(following, HttpStatus.OK);
    }

    // 팔로우 해제
    @DeleteMapping("/follows/{follow_member_id}")
    public ResponseEntity<Void> unfollow(@Auth AuthMember authMember, @PathVariable Long follow_member_id) {
        followService.unfollow(authMember, follow_member_id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
