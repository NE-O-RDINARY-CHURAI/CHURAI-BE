package com.site.churaibe.domain.post.service.command;

import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.image.entity.PostImage;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.ReactionType;
import com.site.churaibe.domain.post.exception.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.tag.entity.Tag;
import com.site.churaibe.global.apiPayload.exception.GeneralException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandService {

    private final PostRepository postRepository;

    public PostResDTO.PostDetailDTO updatePost(Long id, PostReqDTO.PostUpdateDTO dto) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new GeneralException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getPassword().equals(dto.password())) {
            throw new GeneralException(PostErrorCode.PASSWORD_MISMATCH);
        }

        post.update(dto.title(), dto.contents(), dto.category());

        if (dto.imageUrls() != null) {
            post.clearImages();
            dto.imageUrls().forEach(url -> {
                PostImage image = PostImage.builder().imageUrl(url).build();
                post.addPostImage(image);
            });
        }

        if (dto.tags() != null) {
            post.clearTags();
            dto.tags().forEach(tagName -> {
                Tag tag = Tag.builder().name(tagName).build();
                post.addTag(tag);
            });
        }

        return toDetailDTO(post);
    }

    private PostResDTO.PostDetailDTO toDetailDTO(Post post) {
        List<String> imageUrls = post.getPostImages().stream().map(PostImage::getImageUrl).toList();
        List<String> tags = post.getTags().stream().map(Tag::getName).toList();
        long churai = post.getReactions().stream().filter(r -> r.getType() == ReactionType.CHURAI).count();
        long interested = post.getReactions().stream().filter(r -> r.getType() == ReactionType.INTERESTED).count();
        List<PostResDTO.CommentResDTO> comments = post.getComments().stream()
            .filter(c -> c.getParent() == null)
            .map(this::toCommentDTO)
            .toList();
        return new PostResDTO.PostDetailDTO(
            post.getId(),
            post.getTitle(),
            post.getContents(),
            post.getNickname(),
            post.getCategory(),
            post.getViews(),
            post.getCreatedAt(),
            imageUrls,
            tags,
            churai,
            interested,
            comments
        );
    }

    private PostResDTO.CommentResDTO toCommentDTO(Comment comment) {
        List<PostResDTO.CommentResDTO> replies = comment.getChildren().stream()
            .map(this::toCommentDTO)
            .toList();
        return new PostResDTO.CommentResDTO(
            comment.getId(),
            comment.getContents(),
            comment.getCreatedAt(),
            replies
        );
    }
}
