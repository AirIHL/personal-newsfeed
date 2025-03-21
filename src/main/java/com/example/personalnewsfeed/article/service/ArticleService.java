package com.example.personalnewsfeed.article.service;

import com.example.personalnewsfeed.article.dto.ArticleRequestDto;
import com.example.personalnewsfeed.article.dto.ArticleResponseDto;
import com.example.personalnewsfeed.article.entity.Article;
import com.example.personalnewsfeed.article.enums.ArticleStatus;
import com.example.personalnewsfeed.article.repository.ArticleRepository;
import com.example.personalnewsfeed.auth.dto.AuthMember;
import com.example.personalnewsfeed.follow.repository.FollowRepository;
import com.example.personalnewsfeed.member.entity.Member;
import com.example.personalnewsfeed.member.repository.MemberRepository;
import com.example.personalnewsfeed.recommend.entity.RecommendArticle;
import com.example.personalnewsfeed.recommend.repository.RecommendArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final RecommendArticleRepository recommendArticleRepository;

    // 게시글 생성
    @Transactional
    public ArticleResponseDto create(AuthMember authMember, String title, String contents) {

        Member member = memberRepository.findById(authMember.getId()).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 회원입니다.")
        );

        Article article = new Article(member, title, contents);

        Article savedArticle = articleRepository.save(article);

        return new ArticleResponseDto(
                savedArticle.getId(),
                savedArticle.getTitle(),
                savedArticle.getContents(),
                savedArticle.getMember().getId()
        );

    }

    // 게시글 다건 조회
    @Transactional(readOnly = true)
    public List<ArticleResponseDto> findAll(int page, int pageSize, LocalDate startDate, LocalDate endDate, String sortBy) {

        // 클라이언트에서 시작 값을 1로 생각하여 1이 들어올 확률이 높다 하지만 jpa가 제공하는 page는 시작 번호가 0번부터 시작한다.
        // 정렬 기준을 선택할 수 있게 하였다.
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, sortBy));

        Page<Article> articlePage;
        if (startDate != null && endDate != null) {

            // 날짜 범위 검색
            LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 23:59:59
            articlePage = articleRepository.findByCreatedAtBetweenAndStatusNot(startDateTime, endDateTime, ArticleStatus.DELETED, pageable);
        } else if (startDate != null) {

            // 시작 날짜만 있는 경우
            LocalDateTime startDateTime = startDate.atStartOfDay();
            articlePage = articleRepository.findByCreatedAtGreaterThanEqualAndStatusNot(startDateTime, ArticleStatus.DELETED, pageable);
        } else if (endDate != null) {

            // 종료 날짜만 있는 경우
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            articlePage = articleRepository.findByCreatedAtLessThanEqualAndStatusNot(endDateTime, ArticleStatus.DELETED, pageable);
        } else {

            // 날짜 필터 없음
            articlePage = articleRepository.findAllByStatusNot(ArticleStatus.DELETED, pageable);
        }

        return articlePage.getContent().stream()
                .map(article -> new ArticleResponseDto(
                        article.getId(), article.getTitle(), article.getContents(), article.getMember().getId()))
                .toList();
    }

    // 게시글 단건 조회
    @Transactional(readOnly = true)
    public ArticleResponseDto findById(Long id) {

         Article article = articleRepository.findById(id).orElseThrow(
                 () -> new IllegalStateException("존재하지 않는 게시글입니다.")
         );

        if (article.getStatus() == ArticleStatus.DELETED) {
            throw new IllegalStateException("삭제된 게시글입니다.");
        }

         return new ArticleResponseDto(article.getId(), article.getTitle(), article.getContents(), article.getMember().getId());
    }

    // 게시글 수정
    @Transactional
    public ArticleResponseDto update(AuthMember authMember, Long id, ArticleRequestDto requestDto) {

        Article article = articleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 게시글입니다.")
        );

        if (!article.getMember().getId().equals(authMember.getId())) {
            throw new IllegalStateException("본인의 게시글만 수정할 수 있습니다.");
        }

        article.update(requestDto.getTitle(), requestDto.getContents());

        return new ArticleResponseDto(article.getId(), article.getTitle(), article.getContents(), article.getMember().getId());
    }

    // 게시글 삭제
    @Transactional
    public void delete(AuthMember authMember, Long id) {

        Article article = articleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 게시글입니다.")
        );

        if (!article.getMember().getId().equals(authMember.getId())) {
            throw new IllegalStateException("본인의 게시글만 삭제할 수 있습니다.");
        }

        if (article.getStatus() == ArticleStatus.DELETED) {
            throw new IllegalStateException("이미 삭제된 글입니다.");
        }

        article.delete();
    }

    // 뉴스피드(팔로우한 사람의 최신 게시물들을 최신순으로)
    @Transactional(readOnly = true)
    public List<ArticleResponseDto> getNewsFeed(AuthMember authMember, int page, int pageSize) {

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 회원입니다."));

        List<Long> followingIds = followRepository.findByMemberAndDeletedAtIsNull(member).stream()
                .map(follow -> follow.getFollowMember().getId())
                .toList();

        if (followingIds.isEmpty()) {
            return List.of(); // 팔로우한 사용자가 없으면 빈 리스트 반환
        }

        // 최신순 정렬
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 팔로우한 사용자의 게시물 조회
        Page<Article> articlePage = articleRepository.findByMemberIdInAndStatusNot(
                followingIds, ArticleStatus.DELETED, pageable);

        return articlePage.getContent().stream()
                .map(article -> new ArticleResponseDto(
                        article.getId(), article.getTitle(), article.getContents(), article.getMember().getId()))
                .toList();
    }

    // 게시글 추천
    @Transactional
    public Integer addRecommendArticle(Long article_id, AuthMember authMember) {

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 회원입니다."));

        Article article = articleRepository.findById(article_id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 게시글입니다.")
        );
        if (article.getStatus() == ArticleStatus.DELETED) {
            throw new IllegalStateException("삭제된 게시글에는 추천할 수 없습니다.");
        }

        if (article.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("본인이 쓴 글에는 추천을 할 수 없습니다.");
        }

        recommendArticleRepository.findByArticleIdAndMemberId(article_id, member.getId()).ifPresent(entity -> {
            throw new IllegalStateException("이미 추천하였습니다.");
        });

        RecommendArticle recommendArticle = new RecommendArticle(member, article);
        recommendArticleRepository.save(recommendArticle);

        article.incrementRecommendCount();  // 추천수를 증가시킴
        articleRepository.save(article);

        return article.getRecommendCount();
    }

    // 게시글 추천 취소
    @Transactional
    public Integer removeRecommendArticle(Long article_id, AuthMember authMember) {

        Member member = memberRepository.findById(authMember.getId()).orElseThrow(
                () -> new IllegalStateException("유효하지 않은 회원입니다."));

        RecommendArticle recommend = recommendArticleRepository.findByArticleIdAndMemberId(article_id, member.getId()).orElseThrow(
                () -> new IllegalStateException("추천하지 않은 게시글입니다."));

        Article article = recommend.getArticle();

        article.decrementRecommendCount();  // 추천수를 감소시킴
        articleRepository.save(article);
        recommendArticleRepository.delete(recommend);

        return article.getRecommendCount();
    }
}
