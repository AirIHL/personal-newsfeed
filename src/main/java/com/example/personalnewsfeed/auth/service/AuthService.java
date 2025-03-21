package com.example.personalnewsfeed.auth.service;

import com.example.personalnewsfeed.auth.dto.AuthRequestDto;
import com.example.personalnewsfeed.auth.dto.AuthResponseDto;
import com.example.personalnewsfeed.auth.dto.SigninRequestDto;
import com.example.personalnewsfeed.common.config.JwtUtil;
import com.example.personalnewsfeed.common.config.PasswordEncoder;
import com.example.personalnewsfeed.member.entity.Member;
import com.example.personalnewsfeed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private String beareToken;

    @Transactional
    public void signup(AuthRequestDto requestDto) {
        // 이미 존재하는 이메일이므로 에러를 던짐
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 회원가입 가능한 이메일
        // 필요한 경우 passwordEncoder를 사용해서 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = new Member(requestDto.getEmail(), requestDto.getName(), encodedPassword, requestDto.getAge(), requestDto.getPhoneNumber(), requestDto.getAddress());
        Member savedMember = memberRepository.save(member);

    }

    @Transactional(readOnly = true)
    public AuthResponseDto signin(SigninRequestDto requestDto) {

        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 회원입니다.")
        );

        String password = requestDto.getPassword();
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalStateException("비밀번호가 잘못되었습니다.");
        }

        //비밀번호가 일치하는 경우 -> 인증됨
        String bearerJwt = jwtUtil.createToken(member.getId(), member.getEmail());
        return new AuthResponseDto(bearerJwt);
    }
}
