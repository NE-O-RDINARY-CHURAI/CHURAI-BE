package com.site.churaibe.domain.post.dto.response;

import com.site.churaibe.domain.post.enums.Category;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class PostResDTO {
    @Builder
    public record PostSaveResDTO(
        Long id
    ) {
    }

    @Builder
    public record PostSummaryDTO(
        Long id,
        String title,
        String nickname,
        Category category,
        Long views,
        LocalDateTime createdAt,
        String thumbnailUrl,
        List<String> tags,
        long churaiCount,
        long interestedCount,
        long commentCount
    ) {}

    @Builder
    public record PostDetailDTO(
        Long id,
        String title,
        String contents,
        String nickname,
        Category category,
        Long views,
        LocalDateTime createdAt,
        List<String> imageUrls,
        List<String> tags,
        long churaiCount,
        long interestedCount,
        List<CommentResDTO> comments
    ) {}

    @Builder
    public record CommentResDTO(
        Long id,
        String contents,
        LocalDateTime createdAt,
        List<CommentResDTO> replies
    ) {}
}
