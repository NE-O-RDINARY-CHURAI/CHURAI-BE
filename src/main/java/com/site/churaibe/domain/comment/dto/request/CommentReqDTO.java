package com.site.churaibe.domain.comment.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CommentReqDTO {
    public record CommentSaveDTO(
        @NotBlank(message = "댓글 내용은 필수 입력 사항입니다.")
        @Schema(description = "댓글 내용", example = "정말 맛있어 보이네요! 다음에 꼭 가봐야겠어요.")
        String contents,

        @Schema(description = "부모 댓글 ID (대댓글인 경우에만 전달, 일반 댓글은 생략 가능)", example = "1")
        Long parentId
    ) {
    }
}
