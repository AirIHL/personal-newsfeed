package com.example.personalnewsfeed.comment.repository;

import com.example.personalnewsfeed.comment.entity.Comment;
import com.example.personalnewsfeed.comment.enums.CommentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByArticleIdAndStatusNot(Long article_id, CommentStatus commentStatus, Pageable pageable);

}
