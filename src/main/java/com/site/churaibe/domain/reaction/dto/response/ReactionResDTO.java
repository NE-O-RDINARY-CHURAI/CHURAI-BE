package com.site.churaibe.domain.reaction.dto.response;

import lombok.Builder;

public class ReactionResDTO {

    @Builder
    public record ReactionSaveResDTO(
        Long id
    ) {}

    public record ReactionCountDTO(
        long churaiCount,
        long interestedCount
    ) {}
}
