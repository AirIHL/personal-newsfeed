package com.example.personalnewsfeed.article.controller;

import com.example.personalnewsfeed.article.dto.ArticleRequestDto;
import com.example.personalnewsfeed.article.dto.ArticleResponseDto;
import com.example.personalnewsfeed.article.service.ArticleService;
import com.example.personalnewsfeed.auth.annotation.Auth;
import com.example.personalnewsfeed.auth.dto.AuthMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // 게시글 생성
    @PostMapping("/articles")
    public ResponseEntity<ArticleResponseDto> createArticle(@Auth AuthMember authMember,@Valid @RequestBody ArticleRequestDto requestDto) {
        ArticleResponseDto savedArticle = articleService.create(authMember, requestDto.getTitle(), requestDto.getContents());
        return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
    }

    // 게시글 다건 조회
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponseDto>> findArticleAll(
            // 쿼리 파라미터를 입력하지 않았을 경우 디폴트 값으로 페이지 1, 데이터 10개씩
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int pageSize,
            @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate startDate,
            @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate endDate,
            @RequestParam (defaultValue = "createdAt") String sortBy
            ) {
        // 페이지와 페이지 사이즈가 0보다 작을경우 디폴트 값으로
        int validPage = (page > 0) ? page : 1;
        int validPageSize = (pageSize > 0) ? pageSize : 10;

        List<ArticleResponseDto> articles = articleService.findAll(validPage, validPageSize, startDate, endDate, sortBy);

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    // 게시글 단건 조회
    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponseDto> findArticleById(@PathVariable Long id) {
        ArticleResponseDto article = articleService.findById(id);

        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleResponseDto> updateArticle(@Auth AuthMember authMember, @PathVariable Long id, @Valid @RequestBody ArticleRequestDto requestDto) {
        ArticleResponseDto updatedArticle = articleService.update(authMember, id, requestDto);

        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@Auth AuthMember authMember, @PathVariable Long id) {
        articleService.delete(authMember, id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 뉴스피드(팔로우한 회원들의 글을 조회하기 위함)
    @GetMapping("/newsfeed")
    public ResponseEntity<List<ArticleResponseDto>> getNewsFeed(
            @Auth AuthMember authMember,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        int validPage = (page > 0) ? page : 1;
        int validPageSize = (pageSize > 0) ? pageSize : 10;

        List<ArticleResponseDto> articles = articleService.getNewsFeed(authMember, validPage, validPageSize);

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    // 게시글 좋아요 등록
    @PostMapping("/articles/{article_id}/recommend")
    public ResponseEntity<Integer> addRecommendArticle(@Auth AuthMember authMember, @PathVariable Long article_id) {

        Integer recommendCount = articleService.addRecommendArticle(article_id, authMember);

        return new ResponseEntity<>(recommendCount, HttpStatus.OK);
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/articles/{article_id}/recommend")
    public ResponseEntity<Integer> removeRecommendArticle(@Auth AuthMember authMember, @PathVariable Long article_id) {

        Integer recommendCount = articleService.removeRecommendArticle(article_id, authMember);

        return new ResponseEntity<>(recommendCount, HttpStatus.OK);
    }
}
