package com.example.personalnewsfeed.recommend.entity;

import com.example.personalnewsfeed.comment.entity.Comment;
import com.example.personalnewsfeed.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RecommendComment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "member_id", nullable = false)
        private Member member;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "comment_id", nullable = false)
        private Comment comment;

    public RecommendComment(Member member, Comment comment) {
        this.member = member;
        this.comment = comment;
    }
}
