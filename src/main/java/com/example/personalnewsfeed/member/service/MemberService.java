package com.example.personalnewsfeed.member.service;

import com.example.personalnewsfeed.auth.dto.AuthMember;
import com.example.personalnewsfeed.common.config.PasswordEncoder;
import com.example.personalnewsfeed.member.dto.MemberRequestDto;
import com.example.personalnewsfeed.member.dto.MemberResponseDto;
import com.example.personalnewsfeed.member.dto.MyProfileResponseDto;
import com.example.personalnewsfeed.member.dto.UpdatePasswordDto;
import com.example.personalnewsfeed.member.entity.Member;
import com.example.personalnewsfeed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 본인 프로필 조회
    @Transactional(readOnly = true)
    public MyProfileResponseDto findMyProfile(AuthMember authMember) {
        Member member = memberRepository.findById(authMember.getId()).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 회원입니다.")
        );

        return new MyProfileResponseDto(member.getId(), member.getEmail(), member.getName(), member.getAge(), member.getPhoneNumber(), member.getAddress());
    }

    // 유저 다건 조회
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findProfileAll() {
        List<Member> members = memberRepository.findAll();

        List<MemberResponseDto> dtos = new ArrayList<>();
        for (Member member : members) {
            MemberResponseDto dto = new MemberResponseDto(member.getId(), member.getEmail(), member.getName());
            dtos.add(dto);
        }
        return dtos;
    }

    // 유저 단건 조회
    @Transactional(readOnly = true)
    public MemberResponseDto findProfileById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 회원입니다.")
        );

        return new MemberResponseDto(member.getId(), member.getEmail(), member.getName());
    }

    // 프로필 업데이트
    @Transactional
    public void update(AuthMember authMember, MemberRequestDto requestDto) {
        Member member = memberRepository.findById(authMember.getId()).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 회원입니다.")
        );

        if (!member.getEmail().equals(requestDto.getEmail()) && memberRepository.existsByEmail(requestDto.getEmail())) {
            throw  new IllegalStateException("이미 존재하는 이메일이기 때문에 업데이트할 수 없습니다.");
        }

        member.update(requestDto.getEmail(), requestDto.getName(), requestDto.getAge(), requestDto.getPhoneNumber(), requestDto.getAddress());
    }

    // 비밀번호 업데이트
    @Transactional
    public void updatePassword(AuthMember authMember, UpdatePasswordDto passwordDto) {
        Member member = memberRepository.findById(authMember.getId()).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 회원입니다.")
        );

        // 입력 받은 새 비밀번호가 원래 비밀번호와 같을 때 예외 처리
        if (passwordEncoder.matches(passwordDto.getNewPassword(), member.getPassword())) {
            throw new IllegalStateException("새 비밀번호가 기존 비밀번호와 같습니다.");
        }

        // 입력받은 예전 비밀번호가 Null이 아니고 원래 비밀번호와 같을 때 비밀번호 업데이트
        if (passwordDto.getOldPassword() != null && passwordEncoder.matches(passwordDto.getOldPassword(), member.getPassword())) {
            member.updatePassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        } else {
            throw new IllegalStateException("기존 비밀번호가 일치하지 않습니다.");
        }

    }
}
