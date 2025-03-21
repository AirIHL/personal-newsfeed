package com.example.personalnewsfeed.article.dto;

import lombok.Getter;

@Getter
public class ArticleResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final Long member_id;

    public ArticleResponseDto(Long id, String title, String contents, Long member_id) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.member_id = member_id;
    }
}
