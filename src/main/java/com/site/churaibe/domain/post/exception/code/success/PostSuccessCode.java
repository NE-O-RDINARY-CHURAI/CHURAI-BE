package com.site.churaibe.domain.post.exception.code.success;

import com.site.churaibe.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostSuccessCode implements BaseSuccessCode {
    POST_SAVE_SUCCESS(
        HttpStatus.OK,
        "POST200_1",
        "게시글이 성공적으로 저장되었습니다."
    ),
    POST_UPDATE_SUCCESS(
        HttpStatus.OK,
        "POST200_2",
        "게시글이 성공적으로 수정되었습니다."
    ),
    POST_DELETE_SUCCESS(
        HttpStatus.OK,
        "POST200_3",
        "게시글이 성공적으로 삭제되었습니다."
    ),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
