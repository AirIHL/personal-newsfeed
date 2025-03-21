package com.example.personalnewsfeed.comment.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String content;
    private final Long member_id;
    private final String memberName;

    public CommentResponseDto(Long id, String content, Long member_id, String memberName) {
        this.id = id;
        this.content = content;
        this.member_id = member_id;
        this.memberName = memberName;
    }
}
