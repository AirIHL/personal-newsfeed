package com.example.personalnewsfeed.follow.repository;

import com.example.personalnewsfeed.follow.entity.Follow;
import com.example.personalnewsfeed.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByMemberAndFollowMemberAndDeletedAtIsNull(Member member, Member followMember);

    List<Follow> findByMemberAndDeletedAtIsNull(Member member);

}
