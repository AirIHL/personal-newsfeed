package com.example.personalnewsfeed.follow.service;

import com.example.personalnewsfeed.auth.dto.AuthMember;
import com.example.personalnewsfeed.follow.entity.Follow;
import com.example.personalnewsfeed.follow.repository.FollowRepository;
import com.example.personalnewsfeed.member.dto.MemberResponseDto;
import com.example.personalnewsfeed.member.entity.Member;
import com.example.personalnewsfeed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void follow(AuthMember authMember, Long follow_member_id) {

        Member member = memberRepository.findById(authMember.getId()).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 회원입니다.")
        );

        Member followMember = memberRepository.findById(follow_member_id).orElseThrow(
                () -> new IllegalStateException("팔로우 하려는 회원이 존재하지 않습니다.")
        );

        if (member.getId().equals(followMember.getId())) {
            throw new IllegalStateException("자기 자신은 팔로우할 수 없습니다.");
        }

        if (followRepository.findByMemberAndFollowMemberAndDeletedAtIsNull(member, followMember).isPresent()) {
            throw new IllegalStateException("이미 팔로우한 회원입니다.");
        }

        Follow follow = new Follow(member, followMember);
        followRepository.save(follow);

    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> getFollowingIds(AuthMember authMember) {

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 회원입니다."));

        List<Follow> follows = followRepository.findByMemberAndDeletedAtIsNull(member);
        List<MemberResponseDto> result = new ArrayList<>();

        for (Follow follow : follows) {
            Member followMember = follow.getFollowMember();
            result.add(new MemberResponseDto(followMember.getId(), followMember.getName()));
        }

        return result;
    }

    @Transactional
    public void unfollow(AuthMember authMember, Long follow_member_id) {

        Member member = memberRepository.findById(authMember.getId()).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 회원입니다.")
        );

        Member followMember = memberRepository.findById(follow_member_id).orElseThrow(
                () -> new IllegalStateException("팔로우 해제하려는 회원이 존재하지 않습니다.")
        );

        Follow follow = followRepository.findByMemberAndFollowMemberAndDeletedAtIsNull(member, followMember)
                .orElseThrow(() -> new IllegalStateException("팔로우한 적이 없는 회원입니다."));

        follow.delete();
    }
}
