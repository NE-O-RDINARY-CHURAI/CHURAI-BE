package com.site.churaibe.domain.reaction.service.command;

import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.reaction.converter.ReactionConverter;
import com.site.churaibe.domain.reaction.dto.request.ReactionReqDTO;
import com.site.churaibe.domain.reaction.dto.response.ReactionResDTO;
import com.site.churaibe.domain.reaction.entity.Reaction;
import com.site.churaibe.domain.reaction.exception.code.error.ReactionErrorCode;
import com.site.churaibe.domain.reaction.repository.ReactionRepository;
import com.site.churaibe.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReactionCommandService {

    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;

    public ReactionResDTO.ReactionSaveResDTO addReaction(Long postId, ReactionReqDTO.ReactionSaveDTO dto) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new GeneralException(ReactionErrorCode.REACTION_POST_NOT_FOUND));

        Reaction reaction = ReactionConverter.toReaction(post, dto.type());
        post.addReaction(reaction);
        Reaction saved = reactionRepository.save(reaction);

        return ReactionResDTO.ReactionSaveResDTO.builder()
            .id(saved.getId())
            .build();
    }
}
