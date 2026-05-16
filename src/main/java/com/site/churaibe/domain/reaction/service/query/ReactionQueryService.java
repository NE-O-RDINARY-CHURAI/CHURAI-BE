package com.site.churaibe.domain.reaction.service.query;

import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.ReactionType;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.reaction.converter.ReactionConverter;
import com.site.churaibe.domain.reaction.dto.response.ReactionResDTO;
import com.site.churaibe.domain.reaction.exception.code.error.ReactionErrorCode;
import com.site.churaibe.domain.reaction.repository.ReactionRepository;
import com.site.churaibe.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionQueryService {

    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;

    // 게시글의 츄라이·흥미 리액션 수를 각각 COUNT 쿼리로 조회
    public ReactionResDTO.ReactionCountDTO getReactions(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new GeneralException(ReactionErrorCode.REACTION_POST_NOT_FOUND));

        long churaiCount = reactionRepository.countByPostAndType(post, ReactionType.CHURAI);
        long interestedCount = reactionRepository.countByPostAndType(post, ReactionType.INTERESTED);

        return ReactionConverter.toReactionCountDTO(churaiCount, interestedCount);
    }
}
