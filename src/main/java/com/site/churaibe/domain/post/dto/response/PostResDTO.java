package com.site.churaibe.domain.post.dto.response;

import lombok.Builder;

public class PostResDTO {
    @Builder
    public record PostSaveResDTO(
        Long id
    ) {
    }
}
