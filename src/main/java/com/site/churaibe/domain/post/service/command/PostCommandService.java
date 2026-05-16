package com.site.churaibe.domain.post.service.command;

import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.image.entity.PostImage;
import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.ReactionType;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.tag.entity.Tag;
import com.site.churaibe.global.apiPayload.exception.GeneralException;
import com.site.churaibe.global.s3.S3Service;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCommandService {

    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final TransactionTemplate transactionTemplate;

    public PostResDTO.PostSaveResDTO savePost(PostReqDTO.PostSaveDTO request) {
        // S3 이미지 업로드 (트랜잭션 외부)
        List<String> imageUrls = new ArrayList<>();
        if (request.images() != null && !request.images().isEmpty()) {
            log.info("[PostCommandService] S3 이미지 업로드 시작 - 개수: {}", request.images().size());
            imageUrls = s3Service.uploadFiles("posts", request.images());
            log.info("[PostCommandService] S3 이미지 업로드 완료 - URL: {}", imageUrls);
        }

        // DB 저장 (트랜잭션 내부)
        final List<String> finalImageUrls = imageUrls;
        Post savedPost = transactionTemplate.execute(status -> {
            log.info("[PostCommandService] 게시글 DB 저장 시작");
            Post post = PostConverter.toPost(request, finalImageUrls);
            return postRepository.save(post);
        });

        log.info("[PostCommandService] 게시글 작성 프로세스 완료 - Post ID: {}", savedPost.getId());
        return PostResDTO.PostSaveResDTO.builder()
            .id(savedPost.getId())
            .build();
    }

    @Transactional
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
