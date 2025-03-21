package com.example.personalnewsfeed.comment.controller;

import com.example.personalnewsfeed.auth.annotation.Auth;
import com.example.personalnewsfeed.auth.dto.AuthMember;
import com.example.personalnewsfeed.comment.dto.CommentRequestDto;
import com.example.personalnewsfeed.comment.dto.CommentResponseDto;
import com.example.personalnewsfeed.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/comments/{article_id}")
    public ResponseEntity<CommentResponseDto> createComment(@Auth AuthMember authMember, @PathVariable Long article_id, @Valid @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto savedComment = commentService.create(authMember, article_id, requestDto.getContent());

        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    // 댓글 다건 조회
    @GetMapping("/comments/{article_id}")
    public ResponseEntity<List<CommentResponseDto>> findCommentAll(
            @PathVariable Long article_id,
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int pageSize
    ) {
        int validPage = (page > 0) ? page : 1;
        int validPageSize = (pageSize > 0) ? pageSize : 10;

        List<CommentResponseDto> comments = commentService.findAll(article_id, validPage, validPageSize);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // 댓글 수정
    @PutMapping("/comments/{article_id}/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @Auth AuthMember authMember,
            @PathVariable Long article_id,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
       CommentResponseDto updatedComment = commentService.update(authMember, article_id, id, requestDto);

       return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{article_id}/{id}")
    public ResponseEntity<Void> deleteComment(
            @Auth AuthMember authMember,
            @PathVariable Long article_id,
            @PathVariable Long id
    ) {
        commentService.delete(authMember, article_id, id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 댓글 좋아요 등록
    @PostMapping("/comments/{comment_id}/recommend")
    public ResponseEntity<Integer> addRecommendComment(@Auth AuthMember authMember, @PathVariable Long comment_id) {

        Integer recommendCount = commentService.addRecommendComment(comment_id, authMember);

        return new ResponseEntity<>(recommendCount, HttpStatus.OK);
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/comments/{comment_id}/recommend")
    public ResponseEntity<Integer> removeRecommendComment(@Auth AuthMember authMember, @PathVariable Long comment_id) {

        Integer recommendCount = commentService.removeRecommendComment(comment_id, authMember);

        return new ResponseEntity<>(recommendCount, HttpStatus.OK);
    }
}
