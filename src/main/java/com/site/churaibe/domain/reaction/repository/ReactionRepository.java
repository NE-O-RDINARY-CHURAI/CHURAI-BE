package com.site.churaibe.domain.reaction.repository;

import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.ReactionType;
import com.site.churaibe.domain.reaction.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    long countByPostAndType(Post post, ReactionType type);
}
