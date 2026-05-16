package com.site.churaibe.domain.comment.converter;

import com.site.churaibe.domain.comment.dto.request.CommentReqDTO;
import com.site.churaibe.domain.comment.dto.response.CommentResDTO;
import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.post.entity.Post;

public class CommentConverter {
    public static Comment toComment(CommentReqDTO.CommentSaveDTO request, Post post, Comment parent) {
        return Comment.builder()
            .contents(request.contents())
            .post(post)
            .parent(parent)
            .build();
    }

    public static CommentResDTO.CommentSaveResDTO toCommentSaveResDTO(Comment comment) {
        return CommentResDTO.CommentSaveResDTO.builder()
            .id(comment.getId())
            .build();
    }
}
