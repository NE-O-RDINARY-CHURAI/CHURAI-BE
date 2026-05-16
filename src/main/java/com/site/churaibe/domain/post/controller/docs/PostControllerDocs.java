package com.site.churaibe.domain.post.controller.docs;

import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

    @Operation(summary = "게시글 목록 조회 API", description = "카테고리 필터로 게시글 목록을 조회합니다. 카테고리 미입력 시 전체 조회되며 최신순으로 정렬됩니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    ApiResponse<List<PostResDTO.PostSummaryDTO>> getPosts(
        @Parameter(description = "카테고리 필터 (MAIN_DISH / DESSERT)", example = "MAIN_DISH") Category category
    );

    @Operation(summary = "게시글 랭킹 조회 API", description = "츄라이·흥미 리액션 수 합산 기준 상위 10개 게시글을 반환합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    ApiResponse<List<PostResDTO.PostSummaryDTO>> getRanking();

    @Operation(summary = "게시글 검색 API", description = "제목 또는 내용에 키워드가 포함된 게시글을 검색합니다. 카테고리로 추가 필터링이 가능합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    ApiResponse<List<PostResDTO.PostSummaryDTO>> searchPosts(
        @Parameter(description = "검색 키워드 (제목·내용 포함 검색)", example = "라면", required = true) String keyword,
        @Parameter(description = "카테고리 필터 (선택)", example = "MAIN_DISH") Category category
    );

    @Operation(summary = "게시글 상세 조회 API", description = "게시글 ID로 상세 정보를 조회합니다. 조회 시 조회수가 1 증가합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST404_1", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<PostResDTO.PostDetailDTO> getPost(
        @Parameter(description = "게시글 ID", example = "1", required = true) Long id
    );
}
