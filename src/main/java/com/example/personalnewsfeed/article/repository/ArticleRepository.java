package com.example.personalnewsfeed.article.repository;

import com.example.personalnewsfeed.article.entity.Article;
import com.example.personalnewsfeed.article.enums.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByStatusNot(ArticleStatus status, Pageable pageable);

    Page<Article> findByCreatedAtBetweenAndStatusNot(LocalDateTime startDate, LocalDateTime endDate, ArticleStatus status, Pageable pageable);

    Page<Article> findByCreatedAtGreaterThanEqualAndStatusNot(LocalDateTime startDate, ArticleStatus status, Pageable pageable);

    Page<Article> findByCreatedAtLessThanEqualAndStatusNot(LocalDateTime endDate, ArticleStatus status, Pageable pageable);

    Page<Article> findByMemberIdInAndStatusNot(List<Long> memberIds, ArticleStatus status, Pageable pageable);
}
