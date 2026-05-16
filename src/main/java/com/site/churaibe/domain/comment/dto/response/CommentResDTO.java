package com.site.churaibe.domain.comment.dto.response;

import lombok.Builder;

public class CommentResDTO {
    @Builder
    public record CommentSaveResDTO(
        Long id
    ) {
    }
}
