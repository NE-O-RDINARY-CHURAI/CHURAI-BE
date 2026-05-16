package com.site.churaibe.domain.post.service.command;

import com.site.churaibe.domain.post.entity.PostImage;
import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.request.PostReqDTO;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
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
            // 기존 S3 이미지 URL들 백업
            List<String> oldUrls = post.getPostImages().stream()
                .map(PostImage::getImageUrl)
                .toList();

            post.clearImages();
            dto.imageUrls().forEach(url -> {
                PostImage image = PostImage.builder().imageUrl(url).build();
                post.addPostImage(image);
            });

            // 새 목록에 없는 기존 이미지 파일만 S3에서 삭제
            oldUrls.stream()
                .filter(url -> !dto.imageUrls().contains(url))
                .forEach(s3Service::deleteFile);
        }

        return PostConverter.toDetailDTO(post);
    }

    @Transactional
    public void deletePost(Long id, String password) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new GeneralException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getPassword().equals(password)) {
            throw new GeneralException(PostErrorCode.PASSWORD_MISMATCH);
        }

        // S3에서 연관된 실물 이미지 일괄 삭제
        List<String> imageUrls = post.getPostImages().stream()
            .map(PostImage::getImageUrl)
            .toList();
        s3Service.deleteFiles(imageUrls);

        // DB에서 포스트 삭제 (Cascade로 연관된 PostImage, Comment, Reaction도 일괄 자동 삭제)
        postRepository.delete(post);
    }
}
