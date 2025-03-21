package com.example.personalnewsfeed.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleRequestDto {

    @NotBlank(message = "제목을 입력 해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력 해주세요.")
    private String contents;
}
