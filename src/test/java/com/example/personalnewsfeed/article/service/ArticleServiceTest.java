package com.example.personalnewsfeed.article.service;

import com.example.personalnewsfeed.article.dto.ArticleResponseDto;
import com.example.personalnewsfeed.article.entity.Article;
import com.example.personalnewsfeed.article.enums.ArticleStatus;
import com.example.personalnewsfeed.article.repository.ArticleRepository;
import com.example.personalnewsfeed.auth.dto.AuthMember;
import com.example.personalnewsfeed.follow.repository.FollowRepository;
import com.example.personalnewsfeed.member.entity.Member;
import com.example.personalnewsfeed.member.repository.MemberRepository;
import com.example.personalnewsfeed.recommend.repository.RecommendArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private RecommendArticleRepository recommendArticleRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    void 게시글_생성할_수_있다() {

        // given
        Long memberId = 1L;
        AuthMember authMember = new AuthMember(memberId, "test@google.com");
        String title = "테스트 제목";
        String contents = "테스트 내용";

        Member member = new Member("test@google.com", "Test member", "password123", null, null, null);
        ReflectionTestUtils.setField(member, "id", memberId);

        Article article = new Article(member, title, contents);
        ReflectionTestUtils.setField(article, "id", 1L);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(articleRepository.save(any(Article.class))).willReturn(article);

        // when
        ArticleResponseDto response = articleService.create(authMember, title, contents);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContents()).isEqualTo(contents);
        assertThat(response.getMember_id()).isEqualTo(memberId);

        verify(memberRepository, times(1)).findById(memberId);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void 게시글을_단건_조회할_수_있다() {

        // Given
        Long articleId = 1L;
        Long memberId = 1L;
        String title = "테스트 제목";
        String contents = "테스트 내용";

        Member member = new Member("test@google.com", "Test member", "password123", null, null, null);
        ReflectionTestUtils.setField(member, "id", memberId);

        Article article = new Article(member, title, contents);
        ReflectionTestUtils.setField(article, "id", articleId);
        ReflectionTestUtils.setField(article, "status", ArticleStatus.ACTIVE);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleResponseDto response = articleService.findById(articleId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(articleId);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContents()).isEqualTo(contents);
        assertThat(response.getMember_id()).isEqualTo(memberId);

        verify(articleRepository, times(1)).findById(articleId);
    }

}