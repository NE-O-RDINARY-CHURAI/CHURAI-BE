package com.site.churaibe.domain.post.controller;

import com.site.churaibe.domain.post.controller.docs.PostControllerDocs;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.exception.code.success.PostSuccessCode;
import com.site.churaibe.domain.post.service.command.PostCommandService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController implements PostControllerDocs {

    private final PostCommandService postCommandService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResDTO.PostSaveResDTO> savePost(
        @ModelAttribute @Valid PostReqDTO.PostSaveDTO request
    ) {
        log.info("[PostController] 게시글 작성 요청 수신 - 제목: {}, 작성자: {}", request.title(), request.nickname());
        PostResDTO.PostSaveResDTO response = postCommandService.savePost(request);
        return ApiResponse.onSuccess(PostSuccessCode.POST_SAVE_SUCCESS, response);
    }
}
