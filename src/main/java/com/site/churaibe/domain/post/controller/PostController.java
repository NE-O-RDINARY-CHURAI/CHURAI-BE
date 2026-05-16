package com.site.churaibe.domain.post.controller;

import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.service.query.PostQueryService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import com.site.churaibe.global.apiPayload.code.GeneralSuccessCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;

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
}
