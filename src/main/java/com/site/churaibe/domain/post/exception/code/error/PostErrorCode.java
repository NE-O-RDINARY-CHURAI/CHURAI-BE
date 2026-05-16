package com.site.churaibe.domain.post.exception.code.error;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {
    POST_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "POST404_1",
        "게시글을 찾을 수 없습니다."
    ),
    POST_SAVE_FAILED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "POST500_1",
        "게시글 저장에 실패했습니다."
    ),
    POST_IMAGE_COUNT_EXCEEDED(
        HttpStatus.BAD_REQUEST,
        "POST400_1",
        "이미지는 최대 3장까지만 업로드 가능합니다."
    ),
    PASSWORD_MISMATCH(
        HttpStatus.FORBIDDEN,
        "POST403_1",
        "비밀번호가 일치하지 않습니다."
    ),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
