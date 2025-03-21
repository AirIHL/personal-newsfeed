package com.example.personalnewsfeed.recommend.entity;

import com.example.personalnewsfeed.article.entity.Article;
import com.example.personalnewsfeed.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RecommendArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public RecommendArticle(Member member, Article article) {
        this.member = member;
        this.article = article;
    }
}
