package com.example.personalnewsfeed.comment.service;

import com.example.personalnewsfeed.article.entity.Article;
import com.example.personalnewsfeed.article.enums.ArticleStatus;
import com.example.personalnewsfeed.article.repository.ArticleRepository;
import com.example.personalnewsfeed.auth.dto.AuthMember;
import com.example.personalnewsfeed.comment.dto.CommentRequestDto;
import com.example.personalnewsfeed.comment.dto.CommentResponseDto;
import com.example.personalnewsfeed.comment.entity.Comment;
import com.example.personalnewsfeed.comment.enums.CommentStatus;
import com.example.personalnewsfeed.comment.repository.CommentRepository;
import com.example.personalnewsfeed.member.entity.Member;
import com.example.personalnewsfeed.member.repository.MemberRepository;
import com.example.personalnewsfeed.recommend.entity.RecommendComment;
import com.example.personalnewsfeed.recommend.repository.RecommendCommentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final RecommendCommentRepository recommendCommentRepository;

    // 댓글 생성
    @Transactional
    public CommentResponseDto create(AuthMember authMember, Long article_id, @NotBlank(message = "댓글 내용을 입력해주세요.") String content) {

        Member member = memberRepository.findById(authMember.getId()).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 회원입니다.")
        );

        Article article = articleRepository.findById(article_id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 게시글입니다.")
        );

        if (article.getStatus() == ArticleStatus.DELETED) {
            throw new IllegalStateException("삭제된 게시글에는 댓글을 작성할 수 없습니다.");
        }

        Comment comment = new Comment(member, article, content);

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment.getId(), savedComment.getContent(), savedComment.getMember().getId(), savedComment.getMember().getName());
    }

    // 댓글 다건 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAll(Long article_id, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Comment> commentsPage;

        commentsPage = commentRepository.findByArticleIdAndStatusNot(article_id, CommentStatus.DELETED, pageable);

        return commentsPage.getContent().stream()
                .map(comment -> new CommentResponseDto(comment.getId(), comment.getContent(), comment.getMember().getId(), comment.getMember().getName()))
                .toList();
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto update(AuthMember authMember, Long article_id, Long id, @Valid CommentRequestDto requestDto) {

        Article article = articleRepository.findById(article_id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 게시글입니다.")
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 댓글입니다.")
        );

        if (!comment.getArticle().getId().equals(article_id)) {
            throw new IllegalStateException("해당 게시글에 속하지 않은 댓글입니다.");
        }

        if (!comment.getMember().getId().equals(authMember.getId())) {
            throw new IllegalStateException("본인의 댓글만 수정할 수 있습니다.");
        }

        if (comment.getStatus() == CommentStatus.DELETED) {
            throw new IllegalStateException("삭제된 댓글은 수정할 수 없습니다.");
        }

        comment.update(requestDto.getContent());

        return new CommentResponseDto(comment.getId(), comment.getContent(), comment.getMember().getId(), comment.getMember().getName());
    }

    // 댓글 삭제
    @Transactional
    public void delete(AuthMember authMember, Long article_id, Long id) {

        Article article = articleRepository.findById(article_id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 게시글입니다.")
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 댓글입니다.")
        );

        if (!comment.getArticle().getId().equals(article_id)) {
            throw new IllegalStateException("해당 게시글에 속하지 않은 댓글입니다.");
        }

        if (!comment.getMember().getId().equals(authMember.getId())) {
            throw new IllegalStateException("본인의 댓글만 삭제할 수 있습니다.");
        }

        if (comment.getStatus() == CommentStatus.DELETED) {
            throw new IllegalStateException("이미 삭제된 댓글입니다.");
        }

        comment.delete();
    }

    // 댓글 추천
    @Transactional
    public Integer addRecommendComment(Long comment_id, AuthMember authMember) {

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 회원입니다."));

        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 댓글입니다.")
        );
        if (comment.getStatus() == CommentStatus.DELETED) {
            throw new IllegalStateException("삭제된 댓글에는 추천할 수 없습니다.");
        }

        Article article = comment.getArticle();
        if (article.getStatus() == ArticleStatus.DELETED) {
            throw new IllegalStateException("삭제된 게시글의 댓글에는 추천할 수 없습니다.");
        }

        if (comment.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("본인이 쓴 댓글에는 추천을 할 수 없습니다.");
        }

        recommendCommentRepository.findByCommentIdAndMemberId(comment_id, member.getId()).ifPresent(entity -> {
            throw new IllegalStateException("이미 추천하였습니다.");
        });

        RecommendComment recommendComment = new RecommendComment(member, comment);
        recommendCommentRepository.save(recommendComment);

        comment.incrementRecommendCount();  // 추천수를 증가시킴
        commentRepository.save(comment);

        return comment.getRecommendCount();
    }

    // 댓글 추천 취소
    @Transactional
    public Integer removeRecommendComment(Long comment_id, AuthMember authMember) {

        Member member = memberRepository.findById(authMember.getId()).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 회원입니다."));

        RecommendComment recommend = recommendCommentRepository.findByCommentIdAndMemberId(comment_id, member.getId()).orElseThrow(
                () -> new IllegalStateException("추천하지 않은 댓글입니다."));

        Comment comment = recommend.getComment();

        comment.decrementRecommendCount();  // 추천수를 감소시킴
        commentRepository.save(comment);
        recommendCommentRepository.delete(recommend);

        return comment.getRecommendCount();
    }
}
