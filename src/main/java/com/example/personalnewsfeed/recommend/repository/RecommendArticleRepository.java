package com.example.personalnewsfeed.recommend.repository;

import com.example.personalnewsfeed.recommend.entity.RecommendArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendArticleRepository extends JpaRepository<RecommendArticle, Long> {
    Optional<RecommendArticle> findByArticleIdAndMemberId(Long articleId, Long member_id);

}
