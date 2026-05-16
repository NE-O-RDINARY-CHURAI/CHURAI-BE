package com.site.churaibe.domain.comment.controller;

import com.site.churaibe.domain.comment.controller.docs.CommentControllerDocs;
import com.site.churaibe.domain.comment.dto.request.CommentReqDTO;
import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.domain.comment.exception.code.success.CommentSuccessCode;
import com.site.churaibe.domain.comment.service.CommentCommandService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController implements CommentControllerDocs {

    private final CommentCommandService commentCommandService;

    @Override
    @PostMapping
    public ApiResponse<CommentResDTO.CommentSaveResDTO> saveComment(
        @PathVariable Long postId,
        @RequestBody @Valid CommentReqDTO.CommentSaveDTO request
    ) {
        log.info("[CommentController] 댓글/대댓글 저장 요청 수신 - postId: {}, parentId: {}", postId, request.parentId());
        CommentResDTO.CommentSaveResDTO response = commentCommandService.saveComment(postId, request);
        return ApiResponse.onSuccess(CommentSuccessCode.COMMENT_SAVE_SUCCESS, response);
    }
}
