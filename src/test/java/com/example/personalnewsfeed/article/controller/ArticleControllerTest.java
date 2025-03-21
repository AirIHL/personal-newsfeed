package com.example.personalnewsfeed.article.controller;

import com.example.personalnewsfeed.article.dto.ArticleRequestDto;
import com.example.personalnewsfeed.article.dto.ArticleResponseDto;
import com.example.personalnewsfeed.article.service.ArticleService;
import com.example.personalnewsfeed.auth.dto.AuthMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @Test
    void 게시글_생성_성공() throws Exception {

        // Given
        Long memberId = 1L;
        AuthMember authMember = new AuthMember(memberId, "test@google.com");
        String title = "테스트 제목";
        String contents = "테스트 내용";
        ArticleRequestDto requestDto = new ArticleRequestDto(title, contents);
        ArticleResponseDto responseDto = new ArticleResponseDto(1L, title, contents, memberId);

        // when

        // then
    }

    }