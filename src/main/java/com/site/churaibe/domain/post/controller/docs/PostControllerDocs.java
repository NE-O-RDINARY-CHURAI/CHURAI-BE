package com.site.churaibe.domain.post.controller.docs;

import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "Post", description = "게시글 관련 API")
public interface PostControllerDocs {

    @Operation(summary = "게시글 작성 API", description = "이미지와 태그를 포함하여 게시글을 작성합니다. 이미지는 최대 3장까지 가능합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST400_1", description = "이미지 개수 초과 (최대 3장)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST500_1", description = "게시글 저장에 실패했습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_500_1", description = "S3 파일 업로드 중 오류가 발생했습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<PostResDTO.PostSaveResDTO> savePost(
        @Parameter(description = "게시글 작성 정보 (Multipart Form Data)") @ModelAttribute PostReqDTO.PostSaveDTO request
    );
}
