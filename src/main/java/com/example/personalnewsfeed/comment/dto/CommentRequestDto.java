package com.example.personalnewsfeed.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;
}
