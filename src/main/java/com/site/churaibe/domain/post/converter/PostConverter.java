package com.site.churaibe.domain.post.converter;

import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.image.entity.PostImage;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.ReactionType;
import java.util.List;

public class PostConverter {

    public static Post toPost(PostReqDTO.PostSaveDTO request, List<String> imageUrls) {
        // 기본 Post 엔티티 생성
        Post post = Post.builder()
            .title(request.title())
            .contents(request.contents())
            .nickname(request.nickname() == null || request.nickname().trim().isEmpty() ? "익명" : request.nickname())
            .password(request.password())
            .category(request.category())
            .build();

        // 이미지 매핑 (연관관계 편의 메서드 활용)
        if (imageUrls != null && !imageUrls.isEmpty()) {
            imageUrls.forEach(url -> {
                PostImage postImage = PostImage.builder()
                    .imageUrl(url)
                    .post(post)
                    .build();
                post.addPostImage(postImage);
            });
        }

        return post;
    }

    // 썸네일은 첫 번째 이미지, 목록 카드용 요약 정보만 포함
    public static PostResDTO.PostSummaryDTO toSummaryDTO(Post post) {
        String thumbnail = post.getPostImages().isEmpty() ? null : post.getPostImages().get(0).getImageUrl();
        long churai = post.getReactions().stream().filter(r -> r.getType() == ReactionType.CHURAI).count();
        long interested = post.getReactions().stream().filter(r -> r.getType() == ReactionType.INTERESTED).count();
        return PostResDTO.PostSummaryDTO.builder()
            .id(post.getId())
            .title(post.getTitle())
            .nickname(post.getNickname())
            .category(post.getCategory())
            .views(post.getViews())
            .createdAt(post.getCreatedAt())
            .thumbnailUrl(thumbnail)
            .churaiCount(churai)
            .interestedCount(interested)
            .commentCount(post.getComments().size())
            .build();
    }

    // 상세 페이지용 전체 정보 포함, 최상위 댓글만 필터링 후 대댓글 재귀 포함
    public static PostResDTO.PostDetailDTO toDetailDTO(Post post) {
        List<String> imageUrls = post.getPostImages().stream().map(PostImage::getImageUrl).toList();
        long churai = post.getReactions().stream().filter(r -> r.getType() == ReactionType.CHURAI).count();
        long interested = post.getReactions().stream().filter(r -> r.getType() == ReactionType.INTERESTED).count();
        List<PostResDTO.CommentResDTO> comments = post.getComments().stream()
            .filter(c -> c.getParent() == null)
            .map(PostConverter::toCommentDTO)
            .toList();
        return PostResDTO.PostDetailDTO.builder()
            .id(post.getId())
            .title(post.getTitle())
            .contents(post.getContents())
            .nickname(post.getNickname())
            .category(post.getCategory())
            .views(post.getViews())
            .createdAt(post.getCreatedAt())
            .imageUrls(imageUrls)
            .churaiCount(churai)
            .interestedCount(interested)
            .comments(comments)
            .build();
    }

    // 대댓글을 재귀적으로 replies에 포함
    public static PostResDTO.CommentResDTO toCommentDTO(Comment comment) {
        List<PostResDTO.CommentResDTO> replies = comment.getChildren().stream()
            .map(PostConverter::toCommentDTO)
            .toList();
        return PostResDTO.CommentResDTO.builder()
            .id(comment.getId())
            .contents(comment.getContents())
            .createdAt(comment.getCreatedAt())
            .replies(replies)
            .build();
    }
}
