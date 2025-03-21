package com.example.personalnewsfeed.follow.entity;

import com.example.personalnewsfeed.common.entity.BaseEntity;
import com.example.personalnewsfeed.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_member_id", nullable = false)
    private Member followMember;

    public Follow(Member member, Member followMember) {
        this.member = member;
        this.followMember = followMember;
    }

    public void delete() {
        this.setDeletedAt(LocalDateTime.now());
    }
}
