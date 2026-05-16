package com.site.churaibe.domain.post.converter;

import com.site.churaibe.domain.image.entity.PostImage;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.tag.entity.Tag;
import java.util.List;

public class PostConverter {

    public static Post toPost(PostReqDTO.PostSaveDTO request, List<String> imageUrls) {
        // 기본 Post 엔티티 생성
        Post post = Post.builder()
            .title(request.title())
            .contents(request.contents())
            .nickname(request.nickname())
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

        // 태그 매핑 (누락되었던 부분 수정)
        if (request.tags() != null && !request.tags().isEmpty()) {
            request.tags().forEach(tagName -> {
                Tag tag = Tag.builder()
                    .name(tagName)
                    .post(post)
                    .build();
                post.addTag(tag);
            });
        }

        return post;
    }
}
