package com.site.churaibe.domain.comment.controller.docs;

import com.site.churaibe.domain.comment.dto.request.CommentReqDTO;
import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Comment", description = "댓글 관련 API")
public interface CommentControllerDocs {

    @Operation(summary = "댓글/대댓글 작성 API", description = "특정 게시글에 댓글 또는 대댓글(답글)을 작성합니다. parentId가 없으면 일반 댓글, 존재하면 대댓글로 동작합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST404_1", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT404_1", description = "부모 댓글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT400_2", description = "부모 댓글이 해당 게시글에 소속되어 있지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400_1", description = "검증 오류가 발생했습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<CommentResDTO.CommentSaveResDTO> saveComment(
        @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId,
        @Parameter(description = "댓글 작성 정보 (JSON)") @RequestBody CommentReqDTO.CommentSaveDTO request
    );
}
