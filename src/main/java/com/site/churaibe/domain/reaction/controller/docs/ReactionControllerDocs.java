package com.site.churaibe.domain.reaction.controller.docs;

import com.site.churaibe.domain.reaction.dto.request.ReactionReqDTO;
import com.site.churaibe.domain.reaction.dto.response.ReactionResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reaction", description = "리액션 관련 API")
public interface ReactionControllerDocs {

    @Operation(summary = "리액션 추가 API", description = "게시글에 츄라이(CHURAI) 또는 흥미(INTERESTED) 리액션을 추가합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REACTION200_1", description = "리액션이 성공적으로 추가되었습니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REACTION404_1", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ReactionResDTO.ReactionSaveResDTO> addReaction(
        @Parameter(description = "게시글 ID", example = "1", required = true) Long postId,
        ReactionReqDTO.ReactionSaveDTO dto
    );

    @Operation(summary = "리액션 조회 API", description = "게시글의 츄라이·흥미 리액션 수를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REACTION200_2", description = "리액션 조회에 성공했습니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REACTION404_1", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ReactionResDTO.ReactionCountDTO> getReactions(
        @Parameter(description = "게시글 ID", example = "1", required = true) Long postId
    );
}
