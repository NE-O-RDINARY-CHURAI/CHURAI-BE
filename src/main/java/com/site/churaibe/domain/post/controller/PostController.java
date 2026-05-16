package com.site.churaibe.domain.post.controller;

import com.site.churaibe.domain.post.controller.docs.PostControllerDocs;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.exception.code.success.PostSuccessCode;
import com.site.churaibe.domain.post.service.command.PostCommandService;
import com.site.churaibe.domain.post.service.query.PostQueryService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.apiPayload.code.GeneralSuccessCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController implements PostControllerDocs {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResDTO.PostSaveResDTO> savePost(
        @ModelAttribute @Valid PostReqDTO.PostSaveDTO request
    ) {
        log.info("[PostController] 게시글 작성 요청 수신 - 제목: {}, 작성자: {}", request.title(), request.nickname());
        PostResDTO.PostSaveResDTO response = postCommandService.savePost(request);
        return ApiResponse.onSuccess(PostSuccessCode.POST_SAVE_SUCCESS, response);
    }

    @GetMapping
    public ApiResponse<List<PostResDTO.PostSummaryDTO>> getPosts(
        @RequestParam(required = false) Category category
    ) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, postQueryService.getPosts(category));
    }

    @GetMapping("/ranking")
    public ApiResponse<List<PostResDTO.PostSummaryDTO>> getRanking() {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, postQueryService.getRanking());
    }

    @GetMapping("/search")
    public ApiResponse<List<PostResDTO.PostSummaryDTO>> searchPosts(
        @RequestParam String keyword,
        @RequestParam(required = false) Category category
    ) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, postQueryService.searchPosts(keyword, category));
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResDTO.PostDetailDTO> getPost(@PathVariable Long id) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, postQueryService.getPost(id));
    }

    @Override
    @PatchMapping("/{id}")
    public ApiResponse<PostResDTO.PostDetailDTO> updatePost(
        @PathVariable Long id,
        @RequestBody @Valid PostReqDTO.PostUpdateDTO request
    ) {
        log.info("[PostController] 게시글 수정 요청 수신 - ID: {}", id);
        PostResDTO.PostDetailDTO response = postCommandService.updatePost(id, request);
        return ApiResponse.onSuccess(PostSuccessCode.POST_UPDATE_SUCCESS, response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePost(
        @PathVariable Long id,
        @RequestParam String password
    ) {
        log.info("[PostController] 게시글 삭제 요청 수신 - ID: {}", id);
        postCommandService.deletePost(id, password);
        return ApiResponse.onSuccess(PostSuccessCode.POST_DELETE_SUCCESS, "게시글이 성공적으로 삭제되었습니다.");
    }
}
