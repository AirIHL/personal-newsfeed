package com.example.personalnewsfeed.comment.entity;

import com.example.personalnewsfeed.article.entity.Article;
import com.example.personalnewsfeed.comment.enums.CommentStatus;
import com.example.personalnewsfeed.common.entity.BaseEntity;
import com.example.personalnewsfeed.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer recommendCount;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    public Comment(Member member, Article article, String content) {
        this.member = member;
        this.article = article;
        this.content = content;
        this.status = CommentStatus.ACTIVE;
    }

    public void update(@NotBlank(message = "댓글 내용을 입력해주세요.") String content) {
        this.content = content;
    }

    public void delete() {
        this.status = CommentStatus.DELETED;
        this.setDeletedAt(LocalDateTime.now());
    }

    // 추천수 증가 메서드
    public void incrementRecommendCount() {
        this.recommendCount += 1;
    }

    // 추천수 감소 메서드
    // 음수를 방지하기 위하여 Math.max를 사용하였다.
    public void decrementRecommendCount() {
        this.recommendCount = Math.max(0, this.recommendCount - 1);
    }
}
