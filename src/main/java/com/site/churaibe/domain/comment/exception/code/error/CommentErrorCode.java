package com.site.churaibe.domain.comment.exception.code.error;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {
    COMMENT_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "COMMENT404_1",
        "댓글을 찾을 수 없습니다."
    ),
    COMMENT_SAVE_FAILED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "COMMENT500_1",
        "댓글 저장에 실패했습니다."
    ),
    COMMENT_NOT_BELONG_TO_POST(
        HttpStatus.BAD_REQUEST,
        "COMMENT400_2",
        "해당 댓글은 지정된 게시글에 소속되어 있지 않습니다."
    ),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
