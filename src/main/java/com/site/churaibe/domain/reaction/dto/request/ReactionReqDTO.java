package com.site.churaibe.domain.reaction.dto.request;

import com.site.churaibe.domain.post.enums.ReactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ReactionReqDTO {

    public record ReactionSaveDTO(
        @NotNull(message = "리액션 타입은 필수입니다.")
        @Schema(description = "리액션 타입 (CHURAI: 츄라이 / INTERESTED: 흥미)", example = "CHURAI")
        ReactionType type
    ) {}
}
