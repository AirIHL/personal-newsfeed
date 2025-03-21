package com.example.personalnewsfeed.article.entity;

import com.example.personalnewsfeed.article.enums.ArticleStatus;
import com.example.personalnewsfeed.common.entity.BaseEntity;
import com.example.personalnewsfeed.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "article")
@NoArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer recommendCount = 0;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    public Article(Member member, String title, String contents) {
        this.member = member;
        this.title = title;
        this.contents = contents;
        this.status = ArticleStatus.ACTIVE;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void delete() {
        this.status = ArticleStatus.DELETED;
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
