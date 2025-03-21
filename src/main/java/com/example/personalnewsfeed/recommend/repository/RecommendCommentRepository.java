package com.example.personalnewsfeed.recommend.repository;

import com.example.personalnewsfeed.recommend.entity.RecommendComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendCommentRepository extends JpaRepository<RecommendComment, Long> {
    Optional<RecommendComment> findByCommentIdAndMemberId(Long comment_id, Long id);

}
