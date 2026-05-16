package com.site.churaibe.domain.comment.service;

import com.site.churaibe.domain.comment.converter.CommentConverter;
import com.site.churaibe.domain.comment.dto.request.CommentReqDTO;
import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.comment.exception.code.error.CommentErrorCode;
import com.site.churaibe.domain.comment.repository.CommentRepository;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.exception.code.error.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.domain.comment.exception.CommentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentCommandService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResDTO.CommentSaveResDTO saveComment(Long postId, CommentReqDTO.CommentSaveDTO request) {
        log.info("[CommentCommandService] 댓글 작성 시작 - postId: {}, parentId: {}", postId, request.parentId());

        // 게시글 존재 여부 검증
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CommentException(PostErrorCode.POST_NOT_FOUND));

        // 대댓글인 경우 부모 댓글 존재 여부 검증
        Comment parent = null;
        if (request.parentId() != null) {
            parent = commentRepository.findById(request.parentId())
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

            // 부모 댓글이 요청된 게시글에 소속되어 있는지 검증
            if (!parent.getPost().getId().equals(postId)) {
                throw new CommentException(CommentErrorCode.COMMENT_NOT_BELONG_TO_POST);
            }
        }

        // 엔티티 생성
        Comment comment = CommentConverter.toComment(request, post, parent);

        // 양방향 연관관계 편의 메서드 설정
        post.addComment(comment);
        if (parent != null) {
            parent.addChild(comment);
        }

        // DB 저장
        Comment savedComment = commentRepository.save(comment);

        log.info("[CommentCommandService] 댓글 작성 완료 - commentId: {}", savedComment.getId());
        return CommentConverter.toCommentSaveResDTO(savedComment);
    }
}
