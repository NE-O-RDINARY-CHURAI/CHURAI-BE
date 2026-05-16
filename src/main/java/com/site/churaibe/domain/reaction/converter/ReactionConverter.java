package com.site.churaibe.domain.reaction.converter;

import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.ReactionType;
import com.site.churaibe.domain.reaction.dto.response.ReactionResDTO;
import com.site.churaibe.domain.reaction.entity.Reaction;

public class ReactionConverter {

    public static Reaction toReaction(Post post, ReactionType type) {
        return Reaction.builder()
            .post(post)
            .type(type)
            .build();
    }

    public static ReactionResDTO.ReactionCountDTO toReactionCountDTO(long churaiCount, long interestedCount) {
        return new ReactionResDTO.ReactionCountDTO(churaiCount, interestedCount);
    }
}
