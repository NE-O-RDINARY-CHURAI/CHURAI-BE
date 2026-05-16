package com.site.churaibe.domain.reaction.controller;

import com.site.churaibe.domain.reaction.controller.docs.ReactionControllerDocs;
import com.site.churaibe.domain.reaction.dto.request.ReactionReqDTO;
import com.site.churaibe.domain.reaction.dto.response.ReactionResDTO;
import com.site.churaibe.domain.reaction.exception.code.success.ReactionSuccessCode;
import com.site.churaibe.domain.reaction.service.command.ReactionCommandService;
import com.site.churaibe.domain.reaction.service.query.ReactionQueryService;
import com.site.churaibe.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts/{postId}/reactions")
@RequiredArgsConstructor
public class ReactionController implements ReactionControllerDocs {

    private final ReactionCommandService reactionCommandService;
    private final ReactionQueryService reactionQueryService;

    @Override
    @PostMapping
    public ApiResponse<ReactionResDTO.ReactionSaveResDTO> addReaction(
        @PathVariable Long postId,
        @RequestBody @Valid ReactionReqDTO.ReactionSaveDTO dto
    ) {
        return ApiResponse.onSuccess(ReactionSuccessCode.REACTION_SAVE_SUCCESS,
            reactionCommandService.addReaction(postId, dto));
    }

    @Override
    @GetMapping
    public ApiResponse<ReactionResDTO.ReactionCountDTO> getReactions(
        @PathVariable Long postId
    ) {
        return ApiResponse.onSuccess(ReactionSuccessCode.REACTION_GET_SUCCESS,
            reactionQueryService.getReactions(postId));
    }
}
